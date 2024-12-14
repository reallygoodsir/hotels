<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trivago | Compare Hotels</title>
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

        .hero {
            text-align: center;
            padding: 50px 20px;
            background: url('https://via.placeholder.com/1920x500') no-repeat center center/cover;
            color: white;
        }

        .hero h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
        }

        .hero p {
            font-size: 1.2rem;
            margin-bottom: 30px;
        }

        .search-bar {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 50px; /* Increased gap to 50px */
            background-color: white;
            padding: 10px 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 900px; /* Adjusted width for better layout */
            margin: 0 auto;
            margin-top: 20px;
        }

        .search-bar div {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            font-size: 0.9rem;
            color: #555;
        }

        .search-bar div label {
            margin-bottom: 5px;
            font-size: 0.8rem;
            color: #333;
        }

        .search-bar input, .search-bar select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        /* Increased width for hotel selection div */
        .search-bar div:first-child {
            width: 200px; /* Adjusted width for better visibility */
        }

        /* Increased width for Adults and Children divs */
        .search-bar div:nth-child(4), .search-bar div:nth-child(5) {
            width: 150px; /* Adjusted width for better visibility */
        }

        .search-bar button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
        }

        .search-bar button:hover {
            background-color: #0056b3;
        }

        footer {
            text-align: center;
            padding: 20px;
            background-color: #003580;
            color: white;
            margin-top: 20px;
        }

        /* Hotel List Section */
        .hotel-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin: 30px auto;
            max-width: 1200px;
            padding: 0 20px;
        }

        .hotel-card {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.3s ease-in-out;
        }

        .hotel-card:hover {
            transform: translateY(-10px);
        }

        .hotel-card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .hotel-card .hotel-info {
            padding: 15px;
        }

        .hotel-card .hotel-name {
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .hotel-card .hotel-location {
            font-size: 1rem;
            color: #777;
            margin-bottom: 10px;
        }

        .hotel-card .hotel-price {
            font-size: 1.1rem;
            color: #007bff;
            margin-bottom: 15px;
        }

        .hotel-card a {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            display: block;
            width: 100%;
            text-align: center;
        }

        .hotel-card a:hover {
            background-color: #0056b3;
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

    <section class="hero">
        <h1>Find Your Ideal Hotel</h1>
        <p>Compare prices across hundreds of booking sites</p>
    </section>

    <!-- Form to handle hotel search -->
    <form action="http://localhost:8080/hotels/home" method="POST" class="search-bar">
        <div>
            <label for="destination">Hotel</label>
            <select id="destination" name="destination">
                <option>Poland</option>
                <option>Romania</option>
                <option>Slovakia</option>
            </select>
        </div>

        <div>
            <label for="check-in">Check in</label>
            <input id="check-in" type="date" name="check_in">
        </div>

        <div>
            <label for="check-out">Check out</label>
            <input id="check-out" type="date" name="check_out">
        </div>

        <div>
            <label for="adults">Adults</label>
            <input id="adults" type="number" placeholder="1" min="0" name="adults">
        </div>
        <div>
            <label for="children">Children</label>
            <input id="children" type="number" placeholder="0" min="0" name="children">
        </div>
        <button type="submit">Search</button>
    </form>

    <% if(request.getAttribute("post") != null){ %>
    <!-- Hotel List Section -->
    <section class="hotel-list">
        <div class="hotel-card">
            <img src="https://via.placeholder.com/300x200" alt="Hotel Image">
            <div class="hotel-info">
                <div class="hotel-name">Hotel Warsaw</div>
                <div class="hotel-location">Warsaw, Poland</div>
                <div class="hotel-price">$120 per night</div>
                <!-- View Details Button -->
                <a href="http://localhost:8080/hotels/hotel?hotelId=1" class="view-details-btn">View Details</a>
            </div>
        </div>
        <div class="hotel-card">
            <img src="https://via.placeholder.com/300x200" alt="Hotel Image">
            <div class="hotel-info">
                <div class="hotel-name">Hotel Bucharest</div>
                <div class="hotel-location">Bucharest, Romania</div>
                <div class="hotel-price">$110 per night</div>
                <!-- View Details Button -->
                <a href="http://localhost:8080/hotels/hotel?hotelId=2" class="view-details-btn">View Details</a>
            </div>
        </div>
        <!-- Add more hotel cards as needed -->
    </section>
    <% } %>

    <footer>
        &copy; 2024 Trivago. All Rights Reserved.
    </footer>
</body>
</html>