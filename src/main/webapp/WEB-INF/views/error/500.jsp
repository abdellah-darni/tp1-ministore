<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>500 - Internal Server Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --bg-void: #11111b;
            --text-main: #cdd6f4;
            --text-sub: #a6adc8;

            --warm-peach: #fab387;
            --warm-maroon: #eba0ac;
            --warm-rose: #f38ba8;
        }

        body, html {
            height: 100%;
            width: 100%;
            margin: 0;
            font-family: system-ui, -apple-system, sans-serif;
            background-color: var(--bg-void);
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        .horizon-glow {
            position: absolute;
            bottom: -20vh;
            left: 50%;
            transform: translateX(-50%);
            width: 120vw;
            height: 80vh;
            background: radial-gradient(circle at bottom center,
            var(--warm-peach) 0%,
            var(--warm-rose) 30%,
            rgba(235, 160, 172, 0.2) 60%,
            transparent 80%);
            filter: blur(60px);
            opacity: 0.6;
            z-index: 0;
            animation: breathe 8s ease-in-out infinite alternate;
        }

        .content {
            position: relative;
            z-index: 10;
            text-align: center;
            max-width: 800px;
            padding: 2rem;
        }

        .error-code {
            font-size: 18rem;
            line-height: 0.8;
            font-weight: 900;
            letter-spacing: -10px;
            background: linear-gradient(to bottom, var(--text-main), var(--warm-peach));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 1rem;
            opacity: 0.9;
            filter: drop-shadow(0 0 30px rgba(250, 179, 135, 0.5))
            drop-shadow(0 0 60px rgba(243, 139, 168, 0.3));
        }

        .floater {
            font-size: 4rem;
            color: var(--warm-peach);
            animation: float 6s ease-in-out infinite;
            margin-bottom: 1rem;
            display: inline-block;
            filter: drop-shadow(0 0 15px rgba(250, 179, 135, 0.5));
        }

        h2 {
            font-size: 2.5rem;
            color: var(--text-main);
            font-weight: 300;
            margin-bottom: 1rem;
            letter-spacing: 2px;
            text-transform: uppercase;
            text-shadow: 0 0 20px rgba(235, 160, 172, 0.5),
            0 0 40px rgba(250, 179, 135, 0.3);
        }

        p {
            font-size: 1.2rem;
            color: var(--text-sub);
            margin-bottom: 3rem;
            font-weight: 300;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .btn-warm {
            padding: 1rem 3rem;
            font-size: 1.1rem;
            font-weight: 600;
            color: var(--bg-void);
            background: var(--warm-peach);
            border: none;
            border-radius: 50px;
            text-decoration: none;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: all 0.3s ease;
            box-shadow: 0 0 20px rgba(250, 179, 135, 0.4);
        }

        .btn-warm:hover {
            background: var(--warm-rose);
            box-shadow: 0 0 40px rgba(243, 139, 168, 0.6);
            color: var(--bg-void);
            transform: translateY(-2px);
        }

        .noise {
            position: absolute;
            top: 0; left: 0; width: 100%; height: 100%;
            background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)' opacity='0.05'/%3E%3C/svg%3E");
            z-index: 1;
            pointer-events: none;
        }

        @keyframes breathe {
            0% { opacity: 0.5; transform: translateX(-50%) scale(1); }
            100% { opacity: 0.7; transform: translateX(-50%) scale(1.05); }
        }

        @keyframes float {
            0%, 100% { transform: translateY(0) rotate(-5deg); }
            50% { transform: translateY(-20px) rotate(5deg); }
        }
    </style>
</head>
<body>
<div class="noise"></div>
<div class="horizon-glow"></div>

<div class="content">
    <i class="fas fa-layer-group floater"></i>

    <div class="error-code">500</div>
    <h2>Internal System Error</h2>
    <p>Something warm melted the circuits. We are cooling things down and will be back shortly.</p>

    <a href="${pageContext.request.contextPath}/products" class="btn-warm">
        <i class="fas fa-sync-alt me-2"></i>Refresh System
    </a>
</div>
</body>
</html>