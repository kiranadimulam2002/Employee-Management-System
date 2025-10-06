from django.test import TestCase
from .models import Employee

class EmployeeTest(TestCase):
    def test_create_employee(self):
        emp = Employee.objects.create(emp_id = "E001", name = "John Doe", department = "IT")
        self.assertEqual(emp.name, "John Doe")