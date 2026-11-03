# Browser UI and security walkthrough

## Start

Open this folder in IntelliJ, run `UserManagementApplication`, then open `http://localhost:8081/`. The browser UI is served by Spring Boot from `src/main/resources/static/index.html`.

## Create an admin for the demo

1. Register through the browser. The API assigns new accounts the USER role.
2. In MySQL Workbench, promote your own account:

```sql
UPDATE user_management_api_clean.users
SET user_role = 'ADMIN'
WHERE username = 'your-email@example.com';
```

3. Sign out and sign in again. The dashboard then shows the User Directory and Add/Edit/Delete actions.

## Demo password reset

The sign-in page offers a username plus new password reset for local interview demonstration only. It does not verify account ownership and must not be exposed publicly.

## Access rules

- Registration and the home page are public.
- Signed-in users can view and update their own profile and password.
- ADMIN accounts can manage all users.
- HTTP Basic credentials are sent on each protected API call. The browser holds them in session storage for the current tab session; this demo is intended for localhost.
