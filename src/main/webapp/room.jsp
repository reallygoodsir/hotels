<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simple Search Bar</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .search-bar {
            display: flex;
            align-items: center;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 10px;
            max-width: 600px;
            margin: auto;
            background-color: #f9f9f9;
        }
        .search-bar input[type="text"] {
            border: none;
            outline: none;
            flex: 1;
            font-size: 16px;
            padding: 5px;
        }
        .search-bar button {
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            cursor: pointer;
            font-size: 16px;
        }
        .search-bar button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="search-bar">
        Room
    </div>

    <form class="search-bar" action="reserve" method="GET">
        <button type="submit">Reserve</button>
    </form>
</body>
</html>
