<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Room Details</title>
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

        .room-info {
            padding: 20px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: 20px;
            border-radius: 8px;
        }

        .room-name {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .room-price {
            font-size: 1.2rem;
            color: #007bff;
            margin-bottom: 15px;
        }

        .room-description {
            font-size: 1rem;
            color: #555;
            margin-bottom: 20px;
        }

        .room-image {
            width: 100%;
            height: 400px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .reserve-button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            width: 200px; /* Fixed width for the Reserve button */
            text-align: center;
            margin: 0 auto; /* Centering the button horizontally */
            display: block;
        }

        .reserve-button:hover {
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
        <div class="logo">Trivago</div>
        <nav>
            <a href="#">Home</a>
            <a href="#">About</a>
            <a href="#">Contact</a>
        </nav>
    </header>

    <!-- Room Information Section -->
    <section class="room-info">
        <!-- Room Image -->
        <img src="https://via.placeholder.com/1200x400" alt="Room Image" class="room-image">

        <!-- Room Details -->
        <div class="room-name">Deluxe Room</div>
        <div class="room-price">$150 per night</div>
        <div class="room-description">
            <p>This spacious and luxurious room includes a king-sized bed, an en-suite bathroom, and a stunning view of the city. Perfect for a relaxing stay with all the amenities you need for a comfortable experience.</p>
        </div>
    </section>

    <!-- Reserve Button -->
    <form action="http://localhost:8080/hotels/reserve" method="POST">
        <input type="hidden" name="room_id" value="${room_id}"> <!-- Add room_id dynamically -->
        <input type="hidden" name="hotel_id" value="${hotel_id}"> <!-- Add hotel_id dynamically -->
        <input type="hidden" name="price" value="150"> <!-- Room price dynamically, can be passed from backend -->
        <button type="submit" class="reserve-button">Reserve</button>
    </form>

    <footer>
        &copy; 2024 Trivago. All Rights Reserved.
    </footer>
</body>
</html>
