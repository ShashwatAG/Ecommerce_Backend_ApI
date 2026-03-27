Add-Type -AssemblyName System.Drawing

$width = 2200
$height = 1600
$outputPath = Join-Path $PSScriptRoot "spring-boot-backend-workflow.png"

$bitmap = New-Object System.Drawing.Bitmap($width, $height)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
$graphics.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::ClearTypeGridFit

function New-Color($hex) {
    return [System.Drawing.ColorTranslator]::FromHtml($hex)
}

function New-RoundedRectanglePath([float]$x, [float]$y, [float]$w, [float]$h, [float]$radius) {
    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $diameter = $radius * 2
    $path.AddArc($x, $y, $diameter, $diameter, 180, 90)
    $path.AddArc($x + $w - $diameter, $y, $diameter, $diameter, 270, 90)
    $path.AddArc($x + $w - $diameter, $y + $h - $diameter, $diameter, $diameter, 0, 90)
    $path.AddArc($x, $y + $h - $diameter, $diameter, $diameter, 90, 90)
    $path.CloseFigure()
    return $path
}

function Draw-RoundedBox {
    param(
        [System.Drawing.Graphics]$G,
        [float]$X,
        [float]$Y,
        [float]$W,
        [float]$H,
        [string]$Fill,
        [string]$Border,
        [string]$Title,
        [string[]]$Lines,
        [System.Drawing.Font]$TitleFont,
        [System.Drawing.Font]$BodyFont,
        [System.Drawing.Brush]$TitleBrush,
        [System.Drawing.Brush]$BodyBrush
    )

    $path = New-RoundedRectanglePath $X $Y $W $H 28
    $fillBrush = New-Object System.Drawing.SolidBrush((New-Color $Fill))
    $borderPen = New-Object System.Drawing.Pen((New-Color $Border), 3)
    $G.FillPath($fillBrush, $path)
    $G.DrawPath($borderPen, $path)

    $titleRect = [System.Drawing.RectangleF]::new([float]($X + 28), [float]($Y + 20), [float]($W - 56), [float]44)
    $G.DrawString($Title, $TitleFont, $TitleBrush, $titleRect)

    $lineY = $Y + 78
    foreach ($line in $Lines) {
        $bodyRect = [System.Drawing.RectangleF]::new([float]($X + 30), [float]$lineY, [float]($W - 60), [float]34)
        $G.DrawString($line, $BodyFont, $BodyBrush, $bodyRect)
        $lineY += 36
    }

    $fillBrush.Dispose()
    $borderPen.Dispose()
    $path.Dispose()
}

function Draw-Arrow {
    param(
        [System.Drawing.Graphics]$G,
        [float]$X1,
        [float]$Y1,
        [float]$X2,
        [float]$Y2,
        [string]$Color,
        [bool]$Dashed = $false
    )

    $pen = New-Object System.Drawing.Pen((New-Color $Color), 5)
    $pen.EndCap = [System.Drawing.Drawing2D.LineCap]::ArrowAnchor
    if ($Dashed) {
        $pen.DashStyle = [System.Drawing.Drawing2D.DashStyle]::Dash
    }
    $G.DrawLine($pen, $X1, $Y1, $X2, $Y2)
    $pen.Dispose()
}

$bg = New-Color "#07111f"
$graphics.Clear($bg)

$titleBrush = New-Object System.Drawing.SolidBrush((New-Color "#EAF1FF"))
$bodyBrush = New-Object System.Drawing.SolidBrush((New-Color "#B0C3E8"))
$mutedBrush = New-Object System.Drawing.SolidBrush((New-Color "#8EA5D3"))
$accentPen = New-Color "#4D8DFF"

$titleFont = New-Object System.Drawing.Font("Segoe UI Semibold", 30, [System.Drawing.FontStyle]::Bold)
$subtitleFont = New-Object System.Drawing.Font("Segoe UI", 14, [System.Drawing.FontStyle]::Regular)
$boxTitleFont = New-Object System.Drawing.Font("Segoe UI Semibold", 20, [System.Drawing.FontStyle]::Bold)
$boxBodyFont = New-Object System.Drawing.Font("Segoe UI", 13, [System.Drawing.FontStyle]::Regular)

$graphics.DrawString(
    "Spring Boot Ecommerce Backend Workflow",
    $titleFont,
    $titleBrush,
    [System.Drawing.RectangleF]::new([float]120, [float]60, [float]1300, [float]60)
)
$graphics.DrawString(
    "Request flow and layered architecture",
    $subtitleFont,
    $mutedBrush,
    [System.Drawing.RectangleF]::new([float]122, [float]118, [float]700, [float]28)
)

$centerX = 160
$boxW = 1140
$smallBoxW = 720

$client = @{ X = 370; Y = 190; W = $smallBoxW; H = 120 }
$jwt = @{ X = 470; Y = 355; W = 520; H = 100 }
$api = @{ X = $centerX; Y = 500; W = $boxW; H = 210 }
$service = @{ X = $centerX; Y = 760; W = $boxW; H = 230 }
$repo = @{ X = $centerX; Y = 1040; W = $boxW; H = 190 }
$db = @{ X = 420; Y = 1280; W = 620; H = 120 }
$cross = @{ X = 1400; Y = 560; W = 610; H = 320 }

Draw-RoundedBox -G $graphics -X $client.X -Y $client.Y -W $client.W -H $client.H -Fill "#0D1A31" -Border "#396BC6" -Title "Client Request" -Lines @(
    "Swagger / Postman / Browser"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $jwt.X -Y $jwt.Y -W $jwt.W -H $jwt.H -Fill "#10203D" -Border "#4D8DFF" -Title "JWT Security Filter" -Lines @(
    "Spring Security authentication and token validation"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $api.X -Y $api.Y -W $api.W -H $api.H -Fill "#0D1A31" -Border "#274B86" -Title "API Layer" -Lines @(
    "Auth Controller"
    "Product Controller"
    "Category Controller"
    "Cart Controller"
    "Order Controller"
    "User Controller"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $service.X -Y $service.Y -W $service.W -H $service.H -Fill "#0D1A31" -Border "#274B86" -Title "Service Layer" -Lines @(
    "Auth Service"
    "Product Service"
    "Category Service"
    "Cart Service"
    "Order Service"
    "Authenticated User Service"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $repo.X -Y $repo.Y -W $repo.W -H $repo.H -Fill "#0D1A31" -Border "#274B86" -Title "Repository Layer" -Lines @(
    "User Repository"
    "Product Repository"
    "Category Repository"
    "Cart Repository"
    "Order Repository"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $db.X -Y $db.Y -W $db.W -H $db.H -Fill "#10203D" -Border "#4D8DFF" -Title "Database Layer" -Lines @(
    "PostgreSQL"
    "JPA + Hibernate"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-RoundedBox -G $graphics -X $cross.X -Y $cross.Y -W $cross.W -H $cross.H -Fill "#111D35" -Border "#335EAA" -Title "Cross-Cutting Concerns" -Lines @(
    "Validation"
    "Global Exception Handling"
    "Structured JSON Error Responses"
) -TitleFont $boxTitleFont -BodyFont $boxBodyFont -TitleBrush $titleBrush -BodyBrush $bodyBrush

Draw-Arrow $graphics ($client.X + ($client.W / 2)) ($client.Y + $client.H) ($jwt.X + ($jwt.W / 2)) $jwt.Y "#4D8DFF"
Draw-Arrow $graphics ($jwt.X + ($jwt.W / 2)) ($jwt.Y + $jwt.H) ($api.X + ($api.W / 2)) $api.Y "#4D8DFF"
Draw-Arrow $graphics ($api.X + ($api.W / 2)) ($api.Y + $api.H) ($service.X + ($service.W / 2)) $service.Y "#4D8DFF"
Draw-Arrow $graphics ($service.X + ($service.W / 2)) ($service.Y + $service.H) ($repo.X + ($repo.W / 2)) $repo.Y "#4D8DFF"
Draw-Arrow $graphics ($repo.X + ($repo.W / 2)) ($repo.Y + $repo.H) ($db.X + ($db.W / 2)) $db.Y "#4D8DFF"

Draw-Arrow $graphics ($api.X + $api.W) ($api.Y + 70) $cross.X ($cross.Y + 90) "#6E8FD0" $true
Draw-Arrow $graphics ($service.X + $service.W) ($service.Y + 110) $cross.X ($cross.Y + 190) "#6E8FD0" $true

$footerFont = New-Object System.Drawing.Font("Segoe UI", 11, [System.Drawing.FontStyle]::Regular)
$graphics.DrawString(
    "Built with Spring Boot, Spring Security, JPA, Hibernate, PostgreSQL, DTOs, Lombok, and JWT",
    $footerFont,
    $mutedBrush,
    [System.Drawing.RectangleF]::new([float]120, [float]1490, [float]1500, [float]24)
)

$bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$footerFont.Dispose()
$boxBodyFont.Dispose()
$boxTitleFont.Dispose()
$subtitleFont.Dispose()
$titleFont.Dispose()
$titleBrush.Dispose()
$bodyBrush.Dispose()
$mutedBrush.Dispose()
$graphics.Dispose()
$bitmap.Dispose()

Write-Output $outputPath
