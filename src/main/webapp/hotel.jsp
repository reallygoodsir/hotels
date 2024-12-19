<!DOCTYPE html>
<%@ page import="org.hotels.models.Hotel" %>
<%@ page import="org.hotels.models.HotelInfo" %>
<%@ page import="org.hotels.models.RoomInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hotels.models.Room" %>
<%@ page import="java.math.BigDecimal" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Details</title>
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

        .hotel-info {
            padding: 20px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: 20px;
            border-radius: 8px;
        }

        .hotel-name {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .hotel-location {
            font-size: 1.2rem;
            color: #777;
            margin-bottom: 10px;
        }

        .hotel-price {
            font-size: 1.2rem;
            color: #007bff;
            margin-bottom: 15px;
        }

        .hotel-description {
            font-size: 1rem;
            color: #555;
        }

        .hotel-image {
            width: 100%;
            height: 400px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        /* Rooms Section */
        .rooms-section {
            margin: 30px auto;
            padding: 0 20px;
            max-width: 1200px;
        }

        .rooms-section h2 {
            font-size: 1.5rem;
            margin-bottom: 20px;
        }

        .room-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
        }

        .room-card {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.3s ease-in-out;
        }

        .room-card:hover {
            transform: translateY(-10px);
        }

        .room-card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .room-card .room-info {
            padding: 15px;
        }

        .room-card .room-name {
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .room-card .room-price {
            font-size: 1.1rem;
            color: #007bff;
            margin-bottom: 15px;
        }

        .room-card button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            width: 100%;
        }

        .room-card button:hover {
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

    <section class="hotel-info">
        <img src="https://via.placeholder.com/1200x400" alt="Hotel Image" class="hotel-image">

        <%
            Hotel hotel = (Hotel) session.getAttribute("hotel");
            HotelInfo hotelInfo = hotel.getHotelInfo();
        %>
        <div class="hotel-name"><%= hotel.getName() %></div>
        <div class="hotel-location"><%= request.getParameter("cityName") %>, <%= request.getParameter("countryName") %></div>
        <div class="hotel-description">
            <p><%= hotelInfo.getDetails() %></p>
        </div>
    </section>


    <section class="rooms-section">
        <h2>Rooms Available</h2>
        <div class="room-list">
            <!-- Rooms Loop -->
            <%
                List<Room> hotelRooms = (List<Room>) session.getAttribute("hotelRooms");
                for(Room room : hotelRooms){
                    RoomInfo roomInfo = room.getRoomInfo();
                    BigDecimal bigDecimalPrice = roomInfo.getPricePerNight();
                    float price = bigDecimalPrice.floatValue();
            %>
                    <div class="room-card">
                        <img src="https://via.placeholder.com/300x200" alt="Room Image">
                        <div class="room-info">
                            <div class="room-name"><%= roomInfo.getRoomType() %> Room</div>
                            <div class="room-price">$<%= price %> per night</div>
                            <form action="http://localhost:8080/hotels/room" method="GET">
                                <input type="hidden" name="roomId" value="<%= room.getId() %>">
                                <button type="submit">View Details</button>
                            </form>
                        </div>
                    </div>
            <%
                }
            %>
        </div>
    </section>

    <footer>
        &copy; 2024 Trivago. All Rights Reserved.
    </footer>
</body>
</html>
