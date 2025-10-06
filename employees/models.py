from django.db import models

# Create your models here.

class Employee(models.Model):
    emp_id = models.CharField(max_length=10, unique=True)
    name = models.CharField(max_length=100)
    department = models.CharField(max_length=50)
    designation = models.CharField(max_length=50)
    date_joined = models.DateField()

    def __str__(self):
        return f"{self.emp_id} - {self.name}"
    
class Salary(models.Model):
    employee = models.ForeignKey(Employee, on_delete=models.CASCADE)
    basic = models.DecimalField(max_digits=10, decimal_places=2)
    hra = models.DecimalField(max_digits=10, decimal_places=2)
    deductions = models.DecimalField(max_digits=10, decimal_places=2)
    net_salary = models.DecimalField(max_digits=10, decimal_places=2)
    pay_date = models.DateField(auto_now_add=True)

    def __str__(self):
        return f"{self.employee.name} - {self.net_salary}"
class SalaryHistory(models.Model):
    employee = models.ForeignKey(Employee, on_delete=models.CASCADE)
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    effective_data = models.DateField(auto_now_add=True)