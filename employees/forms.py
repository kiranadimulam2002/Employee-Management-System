from django import forms

from .models import Employee, Salary

class EmployeeForm(forms.ModelForm):
    class Meta:
        model = Employee
        fields = ['emp_id', 'name', 'department', 'designation', 'date_joined']
        widgets = {
            'date_joined': forms.DateInput(attrs={'type': 'date', 'class': 'form-control'}),
            'emp_id': forms.TextInput(attrs={'class': 'form-control'}),
            'name': forms.TextInput(attrs={'class': 'form-control'}),
            'department': forms.TextInput(attrs={'class': 'form-control'}),
            'designation': forms.TextInput(attrs={'class': 'form-control'}),
        }

class SalaryForm(forms.ModelForm):
    class Meta:
        model = Salary
        fields = ['employee', 'basic', 'hra', 'deductions', 'net_salary']
        widgets = {
            'employee': forms.Select(attrs={'class': 'form-control'}),
            'basic': forms.NumberInput(attrs={'class': 'form-control'}),
            'hra': forms.NumberInput(attrs={'class': 'form-control'}),
            'dedeuctions': forms.NumberInput(attrs={'class': 'form-control'}),
            'net_salary': forms.NumberInput(attrs={'class': 'form-control'}),
        }