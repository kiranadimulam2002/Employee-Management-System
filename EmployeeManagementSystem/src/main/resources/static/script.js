console.log("JS Loaded Successfully!");

// ------------------ MOBILE NAV ------------------
const btn = document.getElementById("menuBtn");
const mobileNav = document.getElementById("mobileNav");

if (btn) {
    btn.addEventListener("click", () => {
        mobileNav.classList.toggle("show");
    });
}

// ------------------------------------------------------
// EMPLOYEE TABLE FILTERS
// ------------------------------------------------------
(function () {
    const searchInput = document.getElementById("searchInput");
    const orgFilter = document.getElementById("orgFilter");
    const table = document.getElementById("employeesTable");

    if (!table) return;

    const tbody = table.tBodies[0];

    function matchRow(row, text, org) {
        const cols = Array.from(row.cells).map(c => c.textContent.toLowerCase());
        const combined = cols.join(" ");
        const textOK = !text || combined.includes(text);
        const orgOK = !org || combined.includes(org.toLowerCase());
        return textOK && orgOK;
    }

    function applyFilters() {
        const text = searchInput ? searchInput.value.trim().toLowerCase() : "";
        const org = orgFilter ? orgFilter.value : "";

        Array.from(tbody.rows).forEach(row => {
            row.style.display = matchRow(row, text, org) ? "" : "none";
        });
    }

    if (searchInput) searchInput.addEventListener("input", applyFilters);
    if (orgFilter) orgFilter.addEventListener("change", applyFilters);

})();

// ------------------------------------------------------
// ADD EMPLOYEE (POST)
// ------------------------------------------------------
async function submitEmployeeForm(event) {
    event.preventDefault();

    const employeeData = {
        fullname: document.getElementById("fullname").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        department: document.getElementById("department").value,
        role: document.getElementById("role").value,
        joiningDate: document.getElementById("joiningDate").value,
        salary: parseFloat(document.getElementById("salary").value),
        organization: {
            id: parseInt(document.getElementById("organization").value)
        }
    };

    const response = await fetch("http://localhost:8085/api/employees", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(employeeData),
    });

    if (!response.ok) {
        alert("Failed to add employee");
        return;
    }

    alert("Employee added successfully!");
    window.location.href = "employees_list.html";
}

// ------------------------------------------------------
// LOAD EMPLOYEES LIST
// ------------------------------------------------------
async function loadEmployees() {
    const tableBody = document.getElementById("employeeTableBody");
    if (!tableBody) return;

    const response = await fetch("http://localhost:8085/api/employees");
    const employees = await response.json();

    tableBody.innerHTML = "";

    employees.forEach(emp => {
        tableBody.innerHTML += `
        <tr>
            <td>${emp.employeeId}</td>
            <td>${emp.fullname}</td>
            <td>${emp.email}</td>
            <td>${emp.department}</td>
            <td>${emp.role}</td>
            <td>${emp.salary}</td>
            <td>${emp.organization.name}</td>
            <td class="actions-col">
                <a href="employee_view_edit.html?id=${emp.id}" class="action view">View</a>
                <a href="#" class="action delete delete-btn" data-id="${emp.id}" data-name="${emp.fullname}">Delete</a>
            </td>
        </tr>`;
    });

    // bind delete events after rows created
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            deleteEmployee(btn.dataset.id, btn.dataset.name);
        });
    });
}

loadEmployees();

// ------------------------------------------------------
// LOAD EMPLOYEE DETAILS FOR EDIT
// ------------------------------------------------------
async function loadEmployeeForEdit() {
    const params = new URLSearchParams(window.location.search);
    const empId = params.get("id");

    if (!empId || empId === "null") {
        console.error("Invalid Employee ID in URL");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8085/api/employees/${empId}`);
        if (!response.ok) {
            console.error("Failed to fetch employee");
            return;
        }

        const emp = await response.json();

        // ------------------- Fill Form Fields -------------------
        document.getElementById("employeeId").value = emp.employeeId;
        document.getElementById("fullname").value = emp.fullname;
        document.getElementById("email").value = emp.email;
        document.getElementById("phone").value = emp.phone;
        document.getElementById("department").value = emp.department;
        document.getElementById("role").value = emp.role;
        document.getElementById("joiningDate").value = emp.joiningDate;
        document.getElementById("salary").value = emp.salary;
        document.getElementById("organization").value = emp.organization.id;

        // ------------------- Dynamically Set Navigation Links -------------------
        const salaryLink = document.getElementById("salaryPageLink");
        const payslipLink = document.getElementById("payslipPageLink");
        const transferHistoryLink = document.getElementById("viewTransfersBtn");

        if (salaryLink) salaryLink.href = `salary_management.html?id=${emp.id}`;
        if (payslipLink) payslipLink.href = `payslip.html?id=${emp.id}`;
        if (transferHistoryLink) transferHistoryLink.setAttribute("data-id", emp.id);

    } catch (err) {
        console.error("Error loading employee:", err);
    }
}

loadEmployeeForEdit();


// ------------------------------------------------------
// UPDATE EMPLOYEE (PUT)
// ------------------------------------------------------
async function updateEmployee(event) {
    event.preventDefault();

    const params = new URLSearchParams(window.location.search);
    const empId = params.get("id");

    const updatedEmployee = {
        fullname: document.getElementById("fullname").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        department: document.getElementById("department").value,
        role: document.getElementById("role").value,
        joiningDate: document.getElementById("joiningDate").value,
        salary: parseFloat(document.getElementById("salary").value),
        organization: {
            id: parseInt(document.getElementById("organization").value)
        }
    };

    const response = await fetch(`http://localhost:8085/api/employees/${empId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedEmployee)
    });

    if (!response.ok) {
        alert("Failed to update employee");
        return;
    }

    alert("Updated successfully!");
    window.location.href = "employees_list.html";
}

const editForm = document.getElementById("editEmployeeForm");
if (editForm) editForm.addEventListener("submit", updateEmployee);

// ------------------------------------------------------
// DELETE EMPLOYEE
// ------------------------------------------------------
function deleteEmployee(id, name) {
    if (!confirm(`Delete ${name}?`)) return;

    fetch(`http://localhost:8085/api/employees/${id}`, { method: "DELETE" })
        .then(res => {
            if (!res.ok) {
                alert("Delete failed");
                return;
            }
            alert("Employee deleted");
            loadEmployees();
        })
        .catch(err => console.error(err));
}

// ------------------------------------------------------
// TRANSFER HISTORY POPUP
// ------------------------------------------------------
const viewTransfersBtn = document.getElementById("viewTransfersBtn");
const transferPopup = document.getElementById("transferHistoryPopup");
const closeTransferHistory = document.getElementById("closeTransferHistory");

if (viewTransfersBtn) {
    viewTransfersBtn.addEventListener("click", async () => {
        const params = new URLSearchParams(window.location.search);
        const empId = params.get("id");

        const response = await fetch(`http://localhost:8085/api/employees/${empId}/transfers`);
        const history = await response.json();

        const body = document.getElementById("transferHistoryBody");
        body.innerHTML = "";

        history.forEach(rec => {
            body.innerHTML += `
                <tr>
                    <td>${rec.oldOrganization.name}</td>
                    <td>${rec.newOrganization.name}</td>
                    <td>${rec.transferDate}</td>
                    <td>${rec.reason}</td>
                </tr>`;
        });

        transferPopup.style.display = "block";
    });
}

if (closeTransferHistory) {
    closeTransferHistory.addEventListener("click", () => {
        transferPopup.style.display = "none";
    });
}

// ------------------------------------------------------
// SALARY MANAGEMENT STARTS HERE
// ------------------------------------------------------

// POPUP OPEN/CLOSE
const salaryPopup = document.getElementById("salaryPopup");
const openSalaryBtn = document.getElementById("openSalaryForm");
const closeSalaryBtn = document.getElementById("closeSalaryPopup");

if (openSalaryBtn) openSalaryBtn.onclick = () => salaryPopup.classList.add("show");
if (closeSalaryBtn) closeSalaryBtn.onclick = () => salaryPopup.classList.remove("show");

async function loadEmployeeSalary() {
    const params = new URLSearchParams(window.location.search);
    const empId = params.get("id");

    if (!empId || empId === "null") {
        console.error("Invalid Employee ID for salary page");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8085/api/employees/${empId}`);
        if (!response.ok) {
            console.error("Error loading salary details:", response.status);
            return;
        }

        const emp = await response.json();

        // Fill Salary Header Section
        document.getElementById("salaryEmployeeName").textContent = emp.fullname;
        document.getElementById("salaryOrgName").textContent = emp.organization.name;
        document.getElementById("salaryRole").textContent = emp.role;

        // Current Salary Card
        document.getElementById("currentSalaryAmount").textContent = `₹${emp.salary}`;
        document.getElementById("lastUpdated").textContent = emp.joiningDate; // or emp.lastUpdatedSalary

    } catch (err) {
        console.error("Salary load error:", err);
    }
}

loadEmployeeSalary();


// ------------------------------------------------------
// LOAD SALARY HISTORY
// ------------------------------------------------------
async function loadSalaryHistory() {
    const params = new URLSearchParams(window.location.search);
    const empId = params.get("id");

    if (!empId || empId === "null") {
        console.error("Invalid Employee ID for history");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8085/api/employees/${empId}/salary-history`);
        if (!response.ok) {
            console.error("History API Failed:", response.status);
            return;
        }

        const history = await response.json();
        const tableBody = document.getElementById("salaryHistoryBody");
        tableBody.innerHTML = "";

        if (!Array.isArray(history)) {
            console.error("History is not an array:", history);
            return;
        }

        history.forEach(h => {
            tableBody.innerHTML += `
        <tr>
            <td>${h.effectiveDate}</td>
            <td>₹${h.previousSalary}</td>
            <td>₹${h.newSalary}</td>
            <td>₹${h.incrementAmount}</td>
            <td>${h.reason || "—"}</td>
        </tr>
    `;
        });


    } catch (err) {
        console.error("History load failed:", err);
    }
}

loadSalaryHistory();


// ------------------------------------------------------
// SUBMIT SALARY INCREMENT
// ------------------------------------------------------
async function submitSalaryIncrement(event) {
    event.preventDefault();

    const params = new URLSearchParams(window.location.search);
    const empId = params.get("id");

    const incrementData = {
        newSalary: parseFloat(document.getElementById("incrementSalary").value),
        date: document.getElementById("incrementDate").value,
        reason: document.getElementById("incrementReason").value
    };

    try {
        const response = await fetch(`http://localhost:8085/api/employees/${empId}/increment`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(incrementData)
        });

        if (!response.ok) {
            alert("Failed to update salary");
            return;
        }

        alert("Salary updated successfully!");
        location.reload();

    } catch (err) {
        console.error("Increment failed:", err);
    }
}

const incForm = document.getElementById("incrementForm");
if (incForm) {
    incForm.addEventListener("submit", submitSalaryIncrement);
}

