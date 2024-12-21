<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Reservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        header {
            background-color: #003580;
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header .logo {
            font-size: 1.5rem;
            font-weight: bold;
        }

        header nav a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
            font-size: 1rem;
        }

        .reservation-form {
            max-width: 500px;
            margin: 40px auto;
            background-color: white;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .reservation-form h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .reservation-form label {
            font-size: 1rem;
            margin-bottom: 8px;
            display: block;
        }

        .reservation-form input {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1rem;
        }

        .reservation-form button {
           width: 150px;
           display: block;
           margin: 0 auto;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
        }

        .reservation-form button:hover {
            background-color: #0056b3;
        }

        footer {
            text-align: center;
            padding: 20px;
            background-color: #003580;
            color: white;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <header>
        <div class="logo">Travel Agency</div>
        <nav>
            <a href="http://localhost:8080/hotels/home">Home</a>
            <a href="#">About</a>
            <a href="#">Contact</a>
        </nav>
    </header>

    <!-- Reservation Form Section -->
    <section class="reservation-form">
        <h2>Confirm Your Reservation</h2>
        <%
            if(session.getAttribute("emailConfirmed") == null){
        %>
                <form action="http://localhost:8080/hotels/reserve" method="POST">
                    <%
                        String email = "";
                        String sessionEmail = (String) session.getAttribute("email");
                        if(sessionEmail != null){
                            email = sessionEmail;
                        }
                    %>
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= email %>" required>

                    <%
                        String phoneNumber = "";
                        String sessionPhoneNumber = (String) session.getAttribute("phoneNumber");
                        if(sessionPhoneNumber != null){
                            phoneNumber = sessionPhoneNumber;
                        }
                    %>
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="tel" id="phoneNumber" name="phoneNumber"
                        value="<%= phoneNumber %>" required>

                    <%
                        String name = "";
                        String sessionName = (String) session.getAttribute("name");
                        if(sessionName != null){
                            name = sessionName;
                        }
                    %>
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="<%= name %>" required
                           pattern="[A-Za-z0-9\s\.,'-]+">

                    <button type="submit">Confirm Reserve</button>
                </form>
        <%
            }
            else {
                String invalidEmailCode = (String) request.getAttribute("invalidEmailCode");
                if(invalidEmailCode != null){
        %>
                    <h3>Incorrect verification code</h3>
                <%
                    }
                %>
                    <form action="http://localhost:8080/hotels/reserve-confirmation" method="POST">
                        <label for="emailConfirmationCode">Enter the confirmation number sent to your email:</label>
                        <input type="text" id="emailConfirmationCode" name="emailConfirmationCode" required
                               pattern="\d{4}" maxlength="4" placeholder="1111">
                        <button type="submit">Verify Code</button>
                    </form>
        <%
            }
        %>
    </section>

    <footer>
        &copy; 2024 Travel Agency. All Rights Reserved.
    </footer>
</body>
</html>
