from django.urls import path
from . import views

urlpatterns = [
    path("signup/", views.signup_view, name="signup"),
    path("login/", views.login_view, name="login"),
    path("logout/", views.logout_view, name="logout"),
    path('add/', views.add_employee, name='add_employee'),
    path('add_salary/', views.add_salary, name='add_salary'),
    path('employee/<str:emp_id>/', views.employee_detail, name='employee_detail'),
    path("", views.employee_list, name="employee_list"),
]