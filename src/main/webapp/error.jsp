<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Travel Agency</title>
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

        .error-section {
            text-align: center;
            padding: 50px 20px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: 50px auto;
            max-width: 800px;
            border-radius: 8px;
        }

        .error-section h1 {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 20px;
        }

        .error-section p {
            font-size: 1.2rem;
            color: #555;
            margin-bottom: 30px;
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

    <section class="error-section">
        <h1>An Unexpected Error Occurred</h1>
        <p>Contact a system administrator.</p>
    </section>

    <footer>
        &copy; 2024 Travel Agency. All Rights Reserved.
    </footer>
</body>
</html>
