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
            width: 150px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            width: 200px;
            text-align: center;
            margin: 0 auto;
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
        <div class="logo">Travel Agency</div>
        <nav>
            <a href="http://localhost:8080/hotels/home">Home</a>
            <a href="#">About</a>
            <a href="#">Contact</a>
        </nav>
    </header>

    <!-- Room Information Section -->
    <section class="room-info">
        <!-- Room Image -->
        <%
            String hotelName = (String) session.getAttribute("hotelName");
            hotelName = hotelName.replace(" ", "-");
            String roomNumber = (String) session.getAttribute("roomNumber");
        %>
        <img src="images/<%= hotelName %>_<%= roomNumber %>.png" alt="Room Image" class="room-image">

        <!-- Room Details -->
        <div class="room-name"><%= session.getAttribute("roomType") %> Room</div>
        <div class="room-price">$<%= session.getAttribute("roomPrice") %> per night</div>
        <div class="room-description">
            <p><%= session.getAttribute("roomDetails") %></p>
            <%
                boolean hasAirConditioning = (boolean) session.getAttribute("roomHasAirConditioning");
                if(hasAirConditioning){
            %>
                    <p>Air Conditioning</p>
            <%
                }
            %>
        </div>
    </section>

    <!-- Reserve Button -->
    <form action="http://localhost:8080/hotels/reserve" method="GET">
        <button type="submit" class="reserve-button">Reserve</button>
    </form>

    <footer>
        &copy; 2024 Travel Agency. All Rights Reserved.
    </footer>
</body>
</html>
