# employees/templatetags/custom_tags.py
from django import template

register = template.Library()

@register.filter
def get_salary(salary_data, emp_id):
    return salary_data.get(emp_id)
