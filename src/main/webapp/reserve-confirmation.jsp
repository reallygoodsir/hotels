<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservation Successful</title>
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

        /* Success Message */
        .success-message {
            background-color: #28a745;
            color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            font-size: 1.5rem;
            text-align: center;
            margin-top: 50px;
            width: 50%; /* Fixed width */
            margin-left: auto;
            margin-right: auto;
        }

        /* Return to Home Button */
        .home-button {
            width: 150px;
            display: block;
            margin: 0 auto;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            text-decoration: none;
            text-align: center;
            width: 200px; /* Fixed width */
        }

        .home-button:hover {
            background-color: #0056b3;
        }

        /* Footer */
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

    <!-- Reservation Success Message -->
    <div class="success-message">
        Reservation Successful! <br>
        Your confirmation number is <%= request.getAttribute("confirmationNumber") %>
    </div>

    <!-- Button to Return Home -->
    <a href="http://localhost:8080/hotels/home" class="home-button">Return to Home</a>

    <!-- Footer Section -->
    <footer>
        &copy; 2024 Travel Agency. All Rights Reserved.
    </footer>
</body>
</html>
