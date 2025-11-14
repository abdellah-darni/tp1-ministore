<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - MiniStore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            overflow: hidden;
            height: 100vh;
            width: 100vw;
        }

        /* Split Screen Container */
        .split-container {
            display: flex;
            height: 100vh;
            width: 100vw;
        }

        /* Left Side - Visual */
        .split-left {
            flex: 1;
            background: linear-gradient(135deg, #89b4fa 0%, #94e2d5 50%, #a6e3a1 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        /* Right Side - Form */
        .split-right {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #ffffff;
            padding: 2rem;
            overflow-y: auto;
            max-height: 100vh;
        }

        /* Animated shapes on left side */
        .visual-content {
            position: relative;
            z-index: 2;
            text-align: center;
            padding: 3rem;
            color: white;
        }

        .visual-icon {
            font-size: 8rem;
            margin-bottom: 2rem;
            animation: float 3s ease-in-out infinite;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-20px); }
        }

        .visual-title {
            font-size: 3rem;
            font-weight: 800;
            margin-bottom: 1rem;
            text-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .visual-description {
            font-size: 1.3rem;
            opacity: 0.95;
            max-width: 500px;
            margin: 0 auto;
            line-height: 1.6;
        }

        /* Animated background elements */
        .bg-element {
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(40px);
        }

        .element-1 {
            width: 300px;
            height: 300px;
            top: -100px;
            left: -100px;
            animation: drift 15s infinite ease-in-out;
        }

        .element-2 {
            width: 200px;
            height: 200px;
            bottom: -50px;
            right: -50px;
            animation: drift 20s infinite ease-in-out reverse;
        }

        .element-3 {
            width: 150px;
            height: 150px;
            top: 50%;
            right: 30%;
            animation: drift 18s infinite ease-in-out;
        }

        @keyframes drift {
            0%, 100% { transform: translate(0, 0) rotate(0deg); }
            33% { transform: translate(50px, -30px) rotate(120deg); }
            66% { transform: translate(-30px, 40px) rotate(240deg); }
        }

        /* Form Container */
        .form-container {
            width: 100%;
            max-width: 450px;
        }

        /* Logo */
        .logo-wrapper {
            width: 70px;
            height: 70px;
            margin: 0 auto 1.5rem;
            background: linear-gradient(135deg, #89b4fa 0%, #94e2d5 100%);
            border-radius: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 8px 24px rgba(137, 180, 250, 0.3);
        }

        .logo-wrapper i {
            font-size: 2rem;
            color: white;
        }

        /* Title */
        .form-title {
            background: linear-gradient(135deg, #89b4fa 0%, #94e2d5 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            font-size: 2.2rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
        }

        .form-subtitle {
            color: #6c6f85;
            font-size: 1rem;
            margin-bottom: 2rem;
        }

        /* Form Controls */
        .form-label {
            font-weight: 600;
            color: #4c4f69;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 0.9rem;
        }

        .form-label i {
            color: #89b4fa;
        }

        .input-wrapper {
            position: relative;
            margin-bottom: 1.2rem;
        }

        .form-control {
            border: 2px solid #dce0e8;
            border-radius: 12px;
            padding: 0.75rem 1rem;
            font-size: 0.95rem;
            transition: all 0.3s ease;
            background: #f5f5f5;
            width: 100%;
        }

        .form-control:focus {
            border-color: #89b4fa;
            box-shadow: 0 0 0 4px rgba(137, 180, 250, 0.1);
            background: white;
            outline: none;
        }

        .input-icon {
            position: absolute;
            right: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca0b0;
            transition: all 0.3s ease;
        }

        .form-control:focus ~ .input-icon {
            color: #89b4fa;
        }

        /* Password Strength */
        .password-strength {
            height: 3px;
            background: #e6e9ef;
            border-radius: 2px;
            margin-top: 0.5rem;
            overflow: hidden;
        }

        .password-strength-bar {
            height: 100%;
            width: 0;
            transition: all 0.3s ease;
            border-radius: 2px;
        }

        .strength-weak { width: 33%; background: linear-gradient(90deg, #f38ba8, #fab387); }
        .strength-medium { width: 66%; background: linear-gradient(90deg, #fab387, #f9e2af); }
        .strength-strong { width: 100%; background: linear-gradient(90deg, #a6e3a1, #94e2d5); }

        /* Password Match */
        .password-match {
            font-size: 0.8rem;
            margin-top: 0.5rem;
            display: flex;
            align-items: center;
            gap: 6px;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .password-match.show { opacity: 1; }
        .password-match.match { color: #40a02b; }
        .password-match.no-match { color: #f38ba8; }

        /* Button */
        .btn-submit {
            background: linear-gradient(135deg, #89b4fa 0%, #94e2d5 100%);
            border: none;
            border-radius: 12px;
            padding: 1rem 2rem;
            font-size: 1rem;
            font-weight: 600;
            color: white;
            text-transform: uppercase;
            letter-spacing: 1px;
            box-shadow: 0 8px 24px rgba(137, 180, 250, 0.3);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            width: 100%;
            cursor: pointer;
            margin-top: 0.5rem;
        }

        .btn-submit::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.3);
            transform: translate(-50%, -50%);
            transition: width 0.6s, height 0.6s;
        }

        .btn-submit:hover::before {
            width: 300px;
            height: 300px;
        }

        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 12px 32px rgba(137, 180, 250, 0.4);
        }

        .btn-submit span {
            position: relative;
            z-index: 1;
        }

        /* Alert */
        .alert-custom {
            border: none;
            border-radius: 12px;
            padding: 1rem;
            background: rgba(243, 139, 168, 0.1);
            border-left: 4px solid #f38ba8;
            margin-bottom: 1.2rem;
            animation: shake 0.5s ease-in-out;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-8px); }
            75% { transform: translateX(8px); }
        }

        /* Link */
        .signin-link {
            text-align: center;
            padding: 1rem;
            background: linear-gradient(135deg, rgba(137, 180, 250, 0.05) 0%, rgba(148, 226, 213, 0.05) 100%);
            border-radius: 12px;
            margin-top: 1rem;
        }

        .signin-link a {
            color: #89b4fa;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .signin-link a:hover {
            color: #94e2d5;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .split-left {
                display: none;
            }

            .split-right {
                flex: 1;
            }
        }

        /* Custom scrollbar for form side */
        .split-right::-webkit-scrollbar {
            width: 8px;
        }

        .split-right::-webkit-scrollbar-track {
            background: #f1f1f1;
        }

        .split-right::-webkit-scrollbar-thumb {
            background: #89b4fa;
            border-radius: 4px;
        }

        .split-right::-webkit-scrollbar-thumb:hover {
            background: #94e2d5;
        }
    </style>
</head>
<body>
<div class="split-container">
    <!-- Left Side - Visual -->
    <div class="split-left">
        <div class="bg-element element-1"></div>
        <div class="bg-element element-2"></div>
        <div class="bg-element element-3"></div>

        <div class="visual-content">
            <div class="visual-icon">
                <i class="fas fa-rocket"></i>
            </div>
            <h2 class="visual-title">Join MiniStore</h2>
            <p class="visual-description">
                Start your shopping journey with us today.
                Create your account and explore endless possibilities.
            </p>
        </div>
    </div>

    <!-- Right Side - Form -->
    <div class="split-right">
        <div class="form-container">
            <!-- Logo -->
            <div class="logo-wrapper">
                <i class="fas fa-user-plus"></i>
            </div>

            <!-- Title -->
            <div class="text-center">
                <h1 class="form-title">Create Account</h1>
                <p class="form-subtitle">Join MiniStore and start shopping</p>
            </div>

            <!-- Error Alert -->
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-custom alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                <%= request.getAttribute("error") %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% } %>

            <!-- Register Form -->
            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                <div class="input-wrapper">
                    <label for="userName" class="form-label">
                        <i class="fas fa-user"></i>
                        Username
                    </label>
                    <input type="text"
                           class="form-control"
                           id="userName"
                           name="userName"
                           placeholder="Choose a username"
                           required>
                    <i class="fas fa-user-circle input-icon"></i>
                </div>

                <div class="input-wrapper">
                    <label for="email" class="form-label">
                        <i class="fas fa-envelope"></i>
                        Email Address
                    </label>
                    <input type="email"
                           class="form-control"
                           id="email"
                           name="email"
                           placeholder="your.email@example.com"
                           required>
                    <i class="fas fa-at input-icon"></i>
                </div>

                <div class="input-wrapper">
                    <label for="password" class="form-label">
                        <i class="fas fa-lock"></i>
                        Password
                    </label>
                    <input type="password"
                           class="form-control"
                           id="password"
                           name="password"
                           placeholder="Create a strong password"
                           required>
                    <i class="fas fa-key input-icon"></i>
                    <div class="password-strength">
                        <div class="password-strength-bar" id="strengthBar"></div>
                    </div>
                </div>

                <div class="input-wrapper">
                    <label for="confirmPassword" class="form-label">
                        <i class="fas fa-check-circle"></i>
                        Confirm Password
                    </label>
                    <input type="password"
                           class="form-control"
                           id="confirmPassword"
                           name="confirmPassword"
                           placeholder="Re-enter your password"
                           required>
                    <i class="fas fa-shield-alt input-icon"></i>
                    <div class="password-match" id="passwordMatch">
                        <i class="fas fa-times-circle"></i>
                        <span id="matchText">Passwords do not match</span>
                    </div>
                </div>

                <button type="submit" class="btn-submit">
                    <span>Create Account</span>
                </button>
            </form>

            <!-- Sign In Link -->
            <div class="signin-link">
                <p class="mb-0" style="color: #6c6f85;">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/login">
                        Sign In <i class="fas fa-arrow-right ms-1"></i>
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Password strength checker
    const passwordInput = document.getElementById('password');
    const strengthBar = document.getElementById('strengthBar');

    passwordInput.addEventListener('input', function() {
        const password = this.value;
        let strength = 0;

        if (password.length >= 8) strength++;
        if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
        if (password.match(/[0-9]/)) strength++;
        if (password.match(/[^a-zA-Z0-9]/)) strength++;

        strengthBar.className = 'password-strength-bar';

        if (strength <= 1) {
            strengthBar.classList.add('strength-weak');
        } else if (strength <= 3) {
            strengthBar.classList.add('strength-medium');
        } else {
            strengthBar.classList.add('strength-strong');
        }
    });

    // Password match checker
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const passwordMatch = document.getElementById('passwordMatch');
    const matchText = document.getElementById('matchText');
    const matchIcon = passwordMatch.querySelector('i');

    function checkPasswordMatch() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (confirmPassword.length > 0) {
            passwordMatch.classList.add('show');

            if (password === confirmPassword) {
                passwordMatch.classList.remove('no-match');
                passwordMatch.classList.add('match');
                matchIcon.className = 'fas fa-check-circle';
                matchText.textContent = 'Passwords match!';
            } else {
                passwordMatch.classList.remove('match');
                passwordMatch.classList.add('no-match');
                matchIcon.className = 'fas fa-times-circle';
                matchText.textContent = 'Passwords do not match';
            }
        } else {
            passwordMatch.classList.remove('show');
        }
    }

    confirmPasswordInput.addEventListener('input', checkPasswordMatch);
    passwordInput.addEventListener('input', checkPasswordMatch);

    // Form validation
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Passwords do not match! Please check and try again.');
        }
    });
</script>
</body>
</html>