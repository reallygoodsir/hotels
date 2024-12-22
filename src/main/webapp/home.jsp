<!DOCTYPE html>
<%@ page import="org.hotels.models.Country" %>
<%@ page import="org.hotels.models.City" %>
<%@ page import="org.hotels.models.Hotel" %>
<%@ page import="org.hotels.models.HotelAddress" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Travel Agency | Compare Hotels</title>
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
           background: url('images/homeHeader.png') no-repeat center center/cover;
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
           gap: 50px;
           background-color: white;
           padding: 10px 20px;
           border-radius: 10px;
           box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
           max-width: 900px;
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

       .search-bar div:first-child {
           width: 200px;
       }

       .search-bar div:nth-child(4), .search-bar div:nth-child(5) {
           width: 150px;
       }

       .search-bar button {
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

       .hotel-list {
           display: grid;
           grid-template-columns: 1fr;
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

       .view-details {
           width: 150px;
           display: block;
           margin: 10px auto;
           padding: 10px;
           background-color: #007bff;
           color: white;
           text-decoration: none;
           border-radius: 5px;
           font-size: 1rem;
           text-align: center;
           border: none;
           cursor: pointer;
       }

       .error-label {
           background-color: #ffe5e5;
           color: #d8000c;
           padding: 10px;
           border-radius: 5px;
           margin: 10px auto;
           text-align: center;
           font-size: 1rem;
           max-width: 500px;
           box-shadow: 0 4px 8px rgba(255, 0, 0, 0.1);
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

    <section class="hero">
        <h1>Find Your Ideal Hotel</h1>
        <p>Compare prices across hundreds of booking sites</p>
    </section>

    <%
        String checkIn = (String) session.getAttribute("checkIn");
        checkIn = (checkIn == null) ? "2024-01-17" : checkIn;
        String checkOut = (String) session.getAttribute("checkOut");
        checkOut = (checkOut == null) ? "2024-01-24" : checkOut;

        Integer children = (Integer) session.getAttribute("children");
        children = (children == null) ? 0 : children;
        Integer adults = (Integer) session.getAttribute("adults");
        adults = (adults == null) ? 1 : adults;

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String checkInError = (String) session.getAttribute("checkInError");
            String checkOutError = (String) session.getAttribute("checkOutError");
            String childrenCapacityError = (String) session.getAttribute("childrenCapacityError");
            String adultCapacityError = (String) session.getAttribute("adultCapacityError");
            String dayDistanceTooShort = (String) session.getAttribute("dayDistanceTooShort");
            String checkInBeforeCheckOut = (String) session.getAttribute("checkInBeforeCheckOut");

            if (checkInError != null) {
    %>
        <div class="error-label">No check-in provided</div>
    <%
            }
            if (dayDistanceTooShort != null) {
    %>
        <div class="error-label">Your stay duration can't be under a day</div>
    <%
            }
            if (checkInBeforeCheckOut != null) {
    %>
        <div class="error-label">Check-in can't be before check-out</div>
    <%
            }
            if (checkOutError != null) {
    %>
        <div class="error-label">No check-out provided</div>
    <%
            }
            if (childrenCapacityError != null) {
    %>
        <div class="error-label">Children can't be below 0</div>
    <%
            }
            if (adultCapacityError != null) {
    %>
        <div class="error-label">Adults can't be below 1</div>
    <%
            }
        }
        session.removeAttribute("checkInError");
        session.removeAttribute("checkOutError");
        session.removeAttribute("childrenCapacityError");
        session.removeAttribute("adultCapacityError");
        session.removeAttribute("dayDistanceTooShort");
        session.removeAttribute("checkInBeforeCheckOut");
    %>

    <form action="http://localhost:8080/hotels/home" method="POST" class="search-bar">
        <div>
            <label for="destination">Hotel</label>
            <select id="destination" name="destination">
                <%
                    List<Country> countries = (List<Country>) request.getAttribute("allCountries");
                    for (Country country : countries) {
                %>
                <option><%=country.getName()%></option>
                <%
                    }
                %>
            </select>
        </div>
        <div>
            <label for="check-in">Check in</label>
            <input id="check-in" type="date" name="check_in" value="<%= checkIn %>">
        </div>
        <div>
            <label for="check-out">Check out</label>
            <input id="check-out" type="date" name="check_out" value="<%= checkOut %>">
        </div>
        <div>
            <label for="adults">Adults</label>
            <input id="adults" type="number" placeholder="1" min="0" name="adults" value="<%= adults  %>">
        </div>
        <div>
            <label for="children">Children</label>
            <input id="children" type="number" placeholder="0" min="0" name="children" value="<%= children %>">
        </div>
        <button type="submit">Search</button>
    </form>

    <%
            if("POST".equalsIgnoreCase(request.getMethod())){
     %>
        <section class="hotel-list">
            <%
                String countryName = (String) request.getAttribute("countryName");
                Map<String, List<Hotel>> hotelsMap = (Map<String, List<Hotel>>) session.getAttribute("hotels");

                for (Map.Entry<String, List<Hotel>> entry : hotelsMap.entrySet()) {
                    String city = entry.getKey();
                    List<Hotel> hotelList = entry.getValue();

                    for (Hotel hotel : hotelList) {
                        HotelAddress hotelAddress = hotel.getHotelAddress();
                        String hotelName = hotel.getName().replace(" ", "-");
            %>
                        <div class="hotel-card">
                            <img src="images/<%= hotelName %>.png" alt="Hotel Image">
                            <div class="hotel-info">
                                <div class="hotel-name"><%= hotel.getName() %></div>
                                <div class="hotel-location"><%= city %>, <%= countryName %></div>
                                <a href="http://localhost:8080/hotels/hotel?hotelId=<%= hotel.getId() %>&cityName=<%= city %>&countryName=<%= countryName %>"
                                   class="view-details">View Details</a>

                            </div>
                        </div>
            <%
                    }
                }
            %>
        </section>

     <%
         }
     %>

    <footer>
        <p>&copy; 2024 Travel Agency. All rights reserved.</p>
    </footer>
</body>
</html>
