<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>鱼类信息地图</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            padding: 0;
            position: relative; /* Ensure body is relative for absolute positioning */
        }
        #mapContainer {
            width: 100%;
            height: 100%;
        }
        #searchContainer {
            position: absolute; /* Absolute positioning for form */
            background-color: #dbe2ef;
            border: 1px solid #ccc;
            z-index: 100;
        }
        #formContainer {
            top: 10px;
            left: 10px;
            position: absolute; /* Absolute positioning for form */
            z-index: 100;
        }
        #searchContainer {
            top: 10px;
            right: 10px;
        }
        #clearContainer {
            position: absolute;
            bottom: 20px;
            left: 10px;
            z-index: 100;
        }

        #logoutContainer {
            position: absolute;
            bottom: 20px;
            right: 10px;
            z-index: 100;
        }
        #panel {
            position: fixed; /* Fixed positioning for panel */
            background-color: #dbe2ef;
            max-height: 61%;
            overflow-y: auto;
            bottom: 10px;
            right: 10px;
            width: 280px;
            z-index: 200; /* Ensure panel is above other elements */
        }
        .toggle-content {
            display: none;
        }
        /* 修改字体颜色 */
        body, html {
            color: #000000; /* 设置默认字体颜色 */
        }
        .form-label {
            color: #000000; /* 设置表单标签字体颜色 */
        }
        .button {
            background-color: #3f72af; /* 设置按钮背景颜色 */
            color: #dbe2ef; /* 设置按钮字体颜色 */
            border: none;
            padding: 10px 20px;
            cursor: pointer;
        }
        .button:hover {
            background-color: #112d4e; /* 按钮悬停时的背景颜色 */
        }
    </style>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css" />
    <script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
</head>
<body>
<div id="container"></div>
<div id="panel"></div>
<script type="text/javascript">
    window._AMapSecurityConfig = {
        securityJsCode: "c020d9da670688c89ca692dc83d18278",
    }
</script>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=4c592dc058049a5095fe2b65b1e3264a&plugin=AMap.Driving"></script>
<script type="text/javascript">
    var map;
    var marker;
    var driving;
    var infoWindow;
    var Center = localStorage.getItem('mapCenter') ? JSON.parse(localStorage.getItem('mapCenter')) : [114.361084, 30.533393];
    var Zoom = localStorage.getItem('mapZoom') ? parseInt(localStorage.getItem('mapZoom')) : 15;

    var redIcon = new AMap.Icon({
        image: 'img/Fish.png', // 鱼标记图标URL
        size: new AMap.Size(43, 33), // 图标大小
        imageSize: new AMap.Size(43, 33) // 图标显示大小
    });

    var blueIcon = new AMap.Icon({
        image: 'img/Man.png', // 人标记图标URL
        size: new AMap.Size(42, 43), // 图标大小
        imageSize: new AMap.Size(42, 43) // 图标显示大小
    });

    function initMap() {
        map = new AMap.Map('mapContainer', {
            zoom: Zoom,
            center: Center,
            mapStyle: 'amap://styles/whitesmoke'
        });

        driving = new AMap.Driving({
            map: map,
            panel: "panel"
        });

        map.on('click', function (e) {
            var lat = e.lnglat.getLat();
            var lng = e.lnglat.getLng();
            document.getElementById('latitude').value = lat;
            document.getElementById('longitude').value = lng;
            if (marker) {
                marker.setMap(null);
            }
            marker = new AMap.Marker({
                position: [lng, lat],
                map: map,
                icon: blueIcon,
                offset: new AMap.Pixel(-21, -43)
            });
        });

        map.on('moveend', function () {
            var center = map.getCenter();
            localStorage.setItem('mapCenter', JSON.stringify([center.lng, center.lat]));
            localStorage.setItem('mapZoom', map.getZoom());
        });
    }

    function addMarker(lat, lng, fishInfo) {

        var marker = new AMap.Marker({
            position: [lng, lat],
            map: map,
            icon: redIcon,
            offset: new AMap.Pixel(-17, -25)
        });
        marker.content = fishInfo;

        marker.on('click', function (e) {
            infoWindow = new AMap.InfoWindow({
                content: e.target.content,
                offset: new AMap.Pixel(0, -30)
            });
            infoWindow.open(map, e.target.getPosition());
        });
    }

    function clearMarkers() {
        map.clearMap();
    }

    function clearLocalStorage() {
        localStorage.removeItem('mapCenter');
        localStorage.removeItem('mapZoom');
        alert('本地存储已清除。地图将在下次加载时重置为默认位置。');
    }

    function searchOneFish() {
        var fishName = document.getElementById('search_fish_name').value;
        var lat = document.getElementById('latitude').value;
        var lng = document.getElementById('longitude').value;

        if (!fishName || !lat || !lng) {
            alert('请点击地图以选择坐标并输入鱼名');
            return;
        }

        fetch('SearchFishInfoServlet?search_fish_name=' + fishName + '&latitude=' + lat + '&longitude=' + lng + "&kind=" + 1)
            .then(response => response.json())
            .then(data => {
                clearMarkers();
                marker = new AMap.Marker({
                    position: [lng, lat],
                    map: map,
                    icon: blueIcon,
                    offset: new AMap.Pixel(-21, -43)
                });
                data.forEach(fish => {
                    var con =
                        "<div>"+
                        "<h3>"+fishName+"</h3>"+
                        "<p>省份:"+fish.province+"</p>"+
                        "<p>城市: "+fish.city+"</p>"+
                        "<p>区县: "+fish.district+"</p>"+
                        "<p>纬度: "+fish.latitude+"</p>"+
                        "<p>经度: "+fish.longitude+"</p>"+
                        "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                        "</div>"
                    ;
                    addMarker(fish.latitude, fish.longitude, con);
                });
            })
            .catch(error => console.error('Error:', error));
    }

    function searchAreaFish() {
        var fishName = document.getElementById('search_fish_name').value;
        var bj = document.getElementById('bj').value;
        var lat = document.getElementById('latitude').value;
        var lng = document.getElementById('longitude').value;

        if (!fishName || !lat || !lng) {
            alert('请点击地图以选择坐标并输入鱼名');
            return;
        }

        if (!bj) {
            bj = 10;
        }

        fetch('SearchFishInfoServlet?search_fish_name=' + fishName + '&latitude=' + lat + '&longitude=' + lng + "&kind=" + 0 + "&bj=" + bj)
            .then(response => response.json())
            .then(data => {
                clearMarkers();
                marker = new AMap.Marker({
                    position: [lng, lat],
                    map: map,
                    icon: blueIcon,
                    offset: new AMap.Pixel(-21, -43)
                });
                data.forEach(fish => {
                    var con =
                        "<div>"+
                        "<h3>"+fishName+"</h3>"+
                        "<p>省份:"+fish.province+"</p>"+
                        "<p>城市: "+fish.city+"</p>"+
                        "<p>区县: "+fish.district+"</p>"+
                        "<p>纬度: "+fish.latitude+"</p>"+
                        "<p>经度: "+fish.longitude+"</p>"+
                        "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                        "</div>"
                    ;
                    addMarker(fish.latitude, fish.longitude, con);
                });
            })
            .catch(error => console.error('Error:', error));
    }

    function searchAllFish() {
        var fishName = document.getElementById('search_fish_name').value;
        var lat = document.getElementById('latitude').value;
        var lng = document.getElementById('longitude').value;

        if (!fishName) {
            fetch('GetFishInfoServlet')
                .then(response => response.json())
                .then(data => {
                    data.forEach(fish => {
                        var con =
                            "<div>"+
                            "<h3>"+fish.fishName+"</h3>"+
                            "<p>省份:"+fish.province+"</p>"+
                            "<p>城市: "+fish.city+"</p>"+
                            "<p>区县: "+fish.district+"</p>"+
                            "<p>纬度: "+fish.latitude+"</p>"+
                            "<p>经度: "+fish.longitude+"</p>"+
                            "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                            "</div>"
                        ;
                        addMarker(fish.latitude, fish.longitude, con);
                    });
                })
                .catch(error => console.error('Error:', error));
        } else if (!lat || !lng) {
            fetch('SearchFishInfoServlet?search_fish_name=' + fishName + '&latitude=30&longitude=114' + "&kind=" + 2)
                .then(response => response.json())
                .then(data => {
                    clearMarkers();
                    data.forEach(fish => {
                        var con =
                            "<div>"+
                            "<h3>"+fishName+"</h3>"+
                            "<p>省份:"+fish.province+"</p>"+
                            "<p>城市: "+fish.city+"</p>"+
                            "<p>区县: "+fish.district+"</p>"+
                            "<p>纬度: "+fish.latitude+"</p>"+
                            "<p>经度: "+fish.longitude+"</p>"+
                            "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                            "</div>"
                        ;
                        addMarker(fish.latitude, fish.longitude, con);
                    });
                })
                .catch(error => console.error('Error:', error));
        } else {
            fetch('SearchFishInfoServlet?search_fish_name=' + fishName + '&latitude=' + lat + '&longitude=' + lng + "&kind=" + 2)
                .then(response => response.json())
                .then(data => {
                    clearMarkers();
                    marker = new AMap.Marker({
                        position: [lng, lat],
                        map: map,
                        icon: blueIcon,
                        offset: new AMap.Pixel(-21, -43)
                    });
                    data.forEach(fish => {
                        var con =
                            "<div>"+
                            "<h3>"+fishName+"</h3>"+
                            "<p>省份:"+fish.province+"</p>"+
                            "<p>城市: "+fish.city+"</p>"+
                            "<p>区县: "+fish.district+"</p>"+
                            "<p>纬度: "+fish.latitude+"</p>"+
                            "<p>经度: "+fish.longitude+"</p>"+
                            "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                            "</div>"
                        ;
                        addMarker(fish.latitude, fish.longitude, con);
                    });
                })
                .catch(error => console.error('Error:', error));
        }
    }

    function startNavigation() {
        var fishName = document.getElementById('search_fish_name').value;
        var lat = document.getElementById('latitude').value;
        var lng = document.getElementById('longitude').value;

        if (!fishName || !lat || !lng) {
            alert('请点击地图以选择坐标并输入鱼名');
            return;
        }

        fetch('SearchFishInfoServlet?search_fish_name=' + fishName + '&latitude=' + lat + '&longitude=' + lng + "&kind=" + 1)
            .then(response => response.json())
            .then(data => {
                clearMarkers();
                marker = new AMap.Marker({
                    position: [lng, lat],
                    map: map,
                    icon: blueIcon,
                    offset: new AMap.Pixel(-21, -43)
                });
                data.forEach(fish => {
                    addMarker(fish.latitude, fish.longitude, fish);
                    driving.search(new AMap.LngLat(lng, lat), new AMap.LngLat(fish.longitude, fish.latitude), function (status, result) {
                        if (status === 'complete') {
                            console.log('路线绘制完成');
                        } else {
                            console.error('获取驾车数据失败：', result);
                        }
                    });
                });
            })
            .catch(error => console.error('Error:', error));
    }

    function stopNavigation() {
        driving.clear(); // Clear the navigation
        console.log('导航已停止');
    }

    function toggleVisibility(id) {
        var element = document.getElementById(id);
        if (element.style.display === 'none' || element.style.display === '') {
            element.style.display = 'block';
        } else {
            element.style.display = 'none';
        }
    }
    function logout() {
        window.location.href = 'index.jsp';
    }
    function refreshMap() {
        // 清空当前地图上的所有标记
        map.clearMap();

        // 重新获取所有鱼类信息并重新添加标记
        fetch('GetFishInfoServlet')
            .then(response => response.json())
            .then(data => {
                data.forEach(fish => {
                    addMarker(fish.latitude, fish.longitude, fish);
                });
            })
            .catch(error => console.error('Error:', error));
    }
    function deleteMarker(lat, lng) {
        if (!lat || !lng) {
            console.error('Latitude or longitude is not defined');
            return;
        }
        fetch("DeleteFishInfoServlet?latitude="+lat+"&longitude="+lng,{method:'DELETE'})
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                alert('删除成功！');
                refreshMap();
            })
            .catch(error => console.error('Error:', error));
    }


</script>
<div id="mapContainer"></div>
<div id="formContainer">
    <div id="formContent" class="toggle-content">
        <form action="AddFishInfoServlet" method="post">
            <label class="form-label" for="fish_name">鱼名:</label>
            <input type="text" id="fish_name" name="fish_name" required><br>
            <label class="form-label" for="latitude">纬度:</label>
            <input type="text" id="latitude" name="latitude" readonly required><br>
            <label class="form-label" for="longitude">经度:</label>
            <input type="text" id="longitude" name="longitude" readonly required><br>
            <label class="form-label" for="province">省份:</label>
            <input type="text" id="province" name="province"><br>
            <label class="form-label" for="city">城市:</label>
            <input type="text" id="city" name="city"><br>
            <label class="form-label" for="street">区县:</label>
            <input type="text" id="street" name="street"><br>
            <input class="button" type="submit" value="添加鱼类信息">
        </form>
    </div>
</div>
<div id="searchContainer">
    <button class="button" onclick="toggleVisibility('searchContent')">搜索鱼类信息</button>
    <div id="searchContent" class="toggle-content">
        <label class="form-label" for="search_fish_name">搜索鱼名:</label>
        <input type="text" id="search_fish_name" name="search_fish_name" required><br>
        <label class="form-label" for="bj">搜索范围:</label>
        <input type="text" id="bj" name="bj"><br>
        <button class="button" onclick="searchOneFish()">搜索单个</button>
        <button class="button" onclick="searchAreaFish()">搜索区域</button>
        <button class="button" onclick="searchAllFish()">搜索全部</button>
        <button class="button" onclick="startNavigation()">开始导航</button>
        <button class="button" onclick="stopNavigation()">停止导航</button>
    </div>
</div>
<div id="clearContainer">
    <button class="button" type="button" onclick="clearLocalStorage()">清除本地存储</button>
</div>
<div id="logoutContainer">
    <button class="button" type="button" onclick="logout()">登出</button>
</div>
<script>
    window.onload = function () {
        initMap();
        fetch('GetFishInfoServlet')
            .then(response => response.json())
            .then(data => {
                data.forEach(fish => {
                    var con =
                        "<div>"+
                        "<h3>"+fish.fishName+"</h3>"+
                        "<p>省份:"+fish.province+"</p>"+
                        "<p>城市: "+fish.city+"</p>"+
                        "<p>区县: "+fish.district+"</p>"+
                        "<p>纬度: "+fish.latitude+"</p>"+
                        "<p>经度: "+fish.longitude+"</p>"+
                        "<button class=\"delete-button\" onclick=\"deleteMarker("+fish.latitude+","+fish.longitude+")\">删除</button>"+
                        "</div>"
                    ;
                    addMarker(fish.latitude, fish.longitude, con);
                });
            })
            .catch(error => console.error('Error:', error));
    }
</script>
</body>
</html>
