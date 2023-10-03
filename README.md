# Expense Tracker Application
This appliction is an Android-based expense tracker that allows users to record and track their expenses. Users can create a new expense item and fill in the details for that item. For example, the user can store the payment amount, picture, description, expense category, date, time, and contact. Besides that, users can generate an expense report for every expense item which can be saved outside the application. Users can also search expense items by category and delete items.  

# Features 
**Database**
<br>
SQLite was used for the application’s database because it is included in Android’s standard library and has some additional Java helper classes. The application is designed from the user’s perspective, and it allows them to insert expenses which will then be stored in the application’s database. Three classes were used for the database which are “ExpenseBaseHelper”, “ExpenseCursrorWrapper” and “ExpenseDbSchema”. Every list item in the database has 6 attributes which are UUID, amount, category, details, date, and contact.
<br>
<br>
**Add Expense**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/125e583e-87c7-440d-a2d7-0257d387ea21)
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/a6ff5f1b-61bf-455f-bb92-d796f20052f3)
<br>
In the List View, the user can add an expense using the “Add” icon on the application’s toolbar. Once a new expense is added, the user can record its details in the Details View. The user can record details such as expense amount, category, description, date, time, picture, and contacts.  Once it is recorded, the user can view the item in the application’s List View. Additionally, if the user did not input the amount and category for an expense, a default value is provided. For example, the default value for an expense is 0.
<br>
<br>
**Assigning a category**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/c5d77079-8040-4f0e-883c-67a4d742d29e)
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/9dcd53f7-5300-48cb-a682-2a6f92932489)
<br>
In the Details View, the user can assign a category to an expense using a drop-down menu. When the menu is selected, the user can choose a category from a list of predetermined 15 categories. Each category will have a unique icon which will be displayed alongside the item’s details in the List View. If no category is selected, a default category will be assigned which is “Others”.
<br>
<br>
**Attaching an image**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/5f9d6324-a33d-4c2b-949d-2d550c8e5cc4)
<br>
In the Details View, the user can attach an image to an expense using the “camera” button. When the button is selected, the user can take a picture using the device’s camera where it will be stored within the application’s database. The user can then view the image in the expense’s Details View.
<br>
<br>
**Picking a date and time**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/8e58ca58-a6a9-45db-9e6f-e5d8a52dcd09)
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/bd6d7202-2a55-4cfc-9e78-28c4ef90d6db)
<br>
In the Details View, the user can pick a time and date for an expense using their respective buttons. Once the button is selected, it will prompt a dialog message to appear. The user can choose a time and date using the dialog messages where it will be then displayed as the button’s text. Users can also view the date and time of an expense in the List View.
<br>
<br>
**Choosing a contact**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/751c3110-1d3f-4ceb-8f3c-05043cb16987)
<br>
In the Details View, the user can attach a contact that is related to that expense using the “Add Contact” button. Once selected, the user can choose a contact from their device’s contact list and the chosen contact’s name will appear as the buttons’ text.
<br>
<br>
**Sending an Expense Report**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/d1d0a757-ffea-44a6-983b-9b5c4ee7297a)
<br>
In the Details View, the user can send an expense report using a few options which are clipboard, Google Drive, Messages, Bluetooth, and Gmail by selecting the “Send Expense Report” button. The report will contain the amount, date and contact of the expense.
<br>
<br>
**Delete expense**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/578627e4-4670-4186-a227-f83b6eecca53)
<br>
In the Details View, the user can delete an expense using the “delete button”. Once the button is selected, it will prompt a confirmation dialog message to ask the user whether they want to delete the expense. If the user selects “delete” in the dialog message, the expenses are deleted from the application’s database. If the user selects “cancel”, the expense will not be deleted.
<br>
<br>
**View number of expenses**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/668c85b2-ca40-4c16-961f-cc95a6c79c06)
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/d56b55e2-88e3-4bf2-8adf-542d21c9fabc)
<br>
In the List View, the user can view the total number of expenses in the application using the “show subtitle” option in the application’s toolbar. Once selected, the total number of expenses will be displayed in the toolbar. If the option is selected again, the total number of expenses will be hidden.
<br>
<br>
**Search expenses**
<br>
![image](https://github.com/Ethanoll47/expense-tracker-application/assets/116264100/355edece-e5ae-40e3-9993-1e9ef1acbfb6)
<br>
In the List View, the user can search through their list of expenses using the “search” icon on the application’s toolbar. Using the search bar, the user can type in the category of expense that they want, and the search results will appear. For example, if the user searches for “transportation”, only expenses with the category “transportation” will appear.
<br>
<br>
**View multiple expenses**
<br>
In the Details View, the user can navigate and view multiple expenses by swiping left and right on the screen. As a result, the previous and next expenses’ Detail View will be displayed.
<br>
<br>
**Modifying expenses**
<br>
In the List View, the user can view and modify the details of an expense by selecting the expense.
 
