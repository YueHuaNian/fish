﻿<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>鱼跃</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Raleway:wght@300;700&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Great+Vibes&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Parisienne&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Allura&display=swap">
    <style>
        :root {
            --white: #e9e9e9;
            --gray: #333;
            --blue: #0367a6;
            --lightblue: #008997;
            --button-radius: 0.7rem;
            --max-width: 758px;
            --max-height: 420px;
            font-size: 16px;
            font-family: 'Raleway', sans-serif;
        }

        body {
            align-items: center;
            background-color: var(--white);
            background: url("img/R.jpg");
            background-attachment: fixed;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
            display: grid;
            height: 100vh;
            place-items: center;
        }

        .form__title {
            font-weight: 300;
            margin: 0;
            margin-bottom: 1.25rem;
        }
        .form__title.special {
            font-family: 'Great Vibes', cursive;
        }

        .link {
            color: var(--gray);
            font-size: 0.9rem;
            margin: 1.5rem 0;
            text-decoration: none;
        }

        .container {
            background-color: var(--white);
            border-radius: var(--button-radius);
            box-shadow: 0 0.9rem 1.7rem rgba(0, 0, 0, 0.25),
            0 0.7rem 0.7rem rgba(0, 0, 0, 0.22);
            height: var(--max-height);
            max-width: var(--max-width);
            overflow: hidden;
            position: relative;
            width: 100%;
        }

        .container__form {
            height: 100%;
            position: absolute;
            top: 0;
            transition: all 0.6s ease-in-out;
        }

        .container--signin {
            left: 0;
            width: 50%;
            z-index: 2;
        }

        .container.right-panel-active .container--signin {
            transform: translateX(100%);
        }

        .container--signup {
            left: 0;
            opacity: 0;
            width: 50%;
            z-index: 1;
        }

        .container.right-panel-active .container--signup {
            animation: show 0.6s;
            opacity: 1;
            transform: translateX(100%);
            z-index: 5;
        }

        .container__overlay {
            height: 100%;
            left: 50%;
            overflow: hidden;
            position: absolute;
            top: 0;
            transition: transform 0.6s ease-in-out;
            width: 50%;
            z-index: 100;
        }

        .container.right-panel-active .container__overlay {
            transform: translateX(-100%);
        }

        .overlay {
            background-color: var(--lightblue);
            background: url("img/R.jpg");
            background-attachment: fixed;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
            height: 100%;
            left: -100%;
            position: relative;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
            width: 200%;
        }

        .container.right-panel-active .overlay {
            transform: translateX(50%);
        }

        .overlay__panel {
            align-items: center;
            display: flex;
            flex-direction: column;
            height: 100%;
            justify-content: center;
            position: absolute;
            text-align: center;
            top: 0;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
            width: 50%;
        }

        .overlay--left {
            transform: translateX(-20%);
        }

        .container.right-panel-active .overlay--left {
            transform: translateX(0);
        }

        .overlay--right {
            right: 0;
            transform: translateX(0);
        }

        .container.right-panel-active .overlay--right {
            transform: translateX(20%);
        }

        .btn {
            background-color: var(--blue);
            background-image: linear-gradient(90deg, var(--blue) 0%, var(--lightblue) 74%);
            border-radius: 20px;
            border: 1px solid var(--blue);
            color: var(--white);
            cursor: pointer;
            font-size: 0.8rem;
            font-weight: bold;
            letter-spacing: 0.1rem;
            padding: 0.9rem 4rem;
            text-transform: uppercase;
            transition: transform 80ms ease-in;
        }

        .form>.btn {
            margin-top: 1.5rem;
        }

        .btn:active {
            transform: scale(0.95);
        }

        .btn:focus {
            outline: none;
        }

        .form {
            background-color: var(--white);
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 3rem;
            height: 100%;
            text-align: center;
        }

        .input {
            background-color: #fff;
            border: none;
            padding: 0.9rem 0.9rem;
            margin: 0.5rem 0;
            width: 100%;
        }

        @keyframes show {
            0%, 49.99% {
                opacity: 0;
                z-index: 1;
            }

            50%, 100% {
                opacity: 1;
                z-index: 5;
            }
        }

        .tagline {
            font-family: 'Raleway', sans-serif;
            font-size: 18px;
            color: #007bff;
            margin-bottom: 30px;
        }

    </style>
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const signInBtn = document.getElementById("signIn");
            const signUpBtn = document.getElementById("signUp");
            const container = document.querySelector(".container");

            signInBtn.addEventListener("click", () => {
                container.classList.remove("right-panel-active");
            });

            signUpBtn.addEventListener("click", () => {
                container.classList.add("right-panel-active");
            });
        });

        function showForm(formId) {
            document.getElementById('registerForm').classList.remove('active');
            document.getElementById('loginForm').classList.remove('active');
            document.getElementById(formId).classList.add('active');

            if (formId === 'registerForm') {
                document.getElementById('toggleRegister').style.display = 'none';
                document.getElementById('toggleLogin').style.display = 'block';
            } else {
                document.getElementById('toggleRegister').style.display = 'block';
                document.getElementById('toggleLogin').style.display = 'none';
            }
        }

        function getParameterByName(name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }

        window.onload = function() {
            const error = getParameterByName('error');
            if (error === 'username_taken') {
                alert('用户名已被使用.');
                showForm('registerForm');
            } else if (error === 'invalid_credentials') {
                alert('不正确的账号或密码.');
                showForm('loginForm');
            } else {
                showForm('loginForm');
            }
        }
    </script>
</head>
<body>
<div class="container">
    <!-- Sign Up -->
    <div class="container__form container--signup">
        <form action="registerLogin" class="form" id="registerForm" method="post">
            <h2 class="form__title special">鱼跃—发现最佳钓点</h2>
            <h2 class="form__title">注册</h2>
            <input type="hidden" name="action" value="register">
            <input type="text" placeholder="用户名" id="reg_username" name="username" class="input" required>
            <input type="password" placeholder="密码" id="reg_password" name="password" class="input" required>
            <button class="btn">注册</button>
        </form>
    </div>

    <!-- Sign In -->
    <div class="container__form container--signin active">
        <form action="registerLogin" class="form" id="loginForm" method="post">
            <h2 class="form__title special">鱼跃—走吧，去钓鱼</h2>
            <h2 class="form__title">登录</h2>
            <input type="hidden" name="action" value="login">
            <input type="text" placeholder="用户名" id="login_username" name="username" class="input" required>
            <input type="password" placeholder="密码" id="login_password" name="password" class="input" required>
            <select id="login_userType" name="userType" class="input">
                <option value="user">用户</option>
                <option value="admin">管理员</option>
            </select>
            <button class="btn">登录</button>
        </form>
    </div>

    <!-- Overlay -->
    <div class="container__overlay">
        <div class="overlay">
            <div class="overlay__panel overlay--left">
                <button class="btn" id="signIn">登录</button>
            </div>
            <div class="overlay__panel overlay--right">
                <button class="btn" id="signUp">注册</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
