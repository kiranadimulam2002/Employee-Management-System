from django.shortcuts import render, redirect
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.contrib.auth import login, logout, authenticate
from django.contrib.auth.decorators import login_required
from django.db.models import Q
from django.shortcuts import render, redirect, get_object_or_404


from .models import Employee, Salary
from .forms import EmployeeForm, SalaryForm


def signup_view(request):
    if request.method == "POST":
        form = UserCreationForm(request.POST)

        if form.is_valid(): 
            user = form.save()
            login(request, user)
            return redirect("employee_list")
    else:
        form = UserCreationForm()
    return render(request, "employees/signup.html", {"form": form})

def login_view(request):
    if request.method == "POST":
        form = AuthenticationForm(data=request.POST)

        if form.is_valid():
            user = form.get_user()
            login(request, user)
            return redirect("employee_list")
    else:
        form = AuthenticationForm()
    return render(request, "employees/login.html", {"form": form})


def logout_view(request):
    logout(request)
    return redirect("login")

from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.db.models import Q
from django.shortcuts import render
from .models import Employee, Salary

@login_required
def employee_list(request):
    # --- read GET params ---
    q = request.GET.get('q', '').strip()               # text search
    dept = request.GET.get('dept', '').strip()         # department filter
    min_salary = request.GET.get('min_salary', '').strip()
    max_salary = request.GET.get('max_salary', '').strip()
    page_num = request.GET.get('page', 1)

    # --- base queryset (apply text and department filters in DB) ---
    employees_qs = Employee.objects.all().order_by('emp_id')
    if q:
        employees_qs = employees_qs.filter(
            Q(name__icontains=q) |
            Q(emp_id__icontains=q) |
            Q(designation__icontains=q)
        )
    if dept:
        employees_qs = employees_qs.filter(department__iexact=dept)

    # --- get latest salary per employee (for employees currently in queryset) ---
    # Query salary rows for these employees; order by employee then by id desc so first occurrence is latest
    salaries = Salary.objects.filter(employee__in=employees_qs).order_by('employee', '-id')
    salary_data = {}
    for s in salaries:
        empid = s.employee.emp_id
        # keep first (latest) salary per employee
        if empid not in salary_data:
            salary_data[empid] = s

    # --- convert QS to list and attach emp.net_salary attribute ---
    employees = list(employees_qs)
    for emp in employees:
        salary_obj = salary_data.get(emp.emp_id)
        emp.net_salary = salary_obj.net_salary if salary_obj else None

    # --- apply numeric salary filtering in Python (small datasets OK) ---
    if min_salary:
        try:
            min_val = float(min_salary)
            employees = [e for e in employees if e.net_salary is not None and float(e.net_salary) >= min_val]
        except ValueError:
            pass  # ignore invalid input
    if max_salary:
        try:
            max_val = float(max_salary)
            employees = [e for e in employees if e.net_salary is not None and float(e.net_salary) <= max_val]
        except ValueError:
            pass

    # --- pagination (10 per page) ---
    paginator = Paginator(employees, 10)
    page_obj = paginator.get_page(page_num)  # safe: returns page even if invalid

    # --- list of departments for the dropdown ---
    departments = Employee.objects.values_list('department', flat=True).distinct()

    context = {
        'employees': page_obj,         # page_obj behaves like a list of employees
        'departments': departments,
        'q': q,
        'dept': dept,
        'min_salary': min_salary,
        'max_salary': max_salary,
    }
    return render(request, 'employees/employee_list.html', context)

@login_required
def employee_detail(request, emp_id):
    employee = get_object_or_404(Employee, emp_id=emp_id)
    salary = Salary.objects.filter(employee=employee).first()
    return render(request, 'employees/employee_detail.html', {
        'employee': employee,
        'salary': salary
    })


def add_employee(request):
    if request.method == 'POST':
        form = EmployeeForm(request.POST)

        if form.is_valid():
            form.save()
            return redirect('employee_list')
    else:
        form = EmployeeForm()
        return render(request, 'employees/add_employee.html', {'form': form})
    

def add_salary(request):
    if request.method == 'POST':
        form = SalaryForm(request.POST)

        if form.is_valid():
            form.save()
            return redirect('employee_list')
        
    else:
        form = SalaryForm()
        return render(request, 'employees/add_salary.html', {'form': form})