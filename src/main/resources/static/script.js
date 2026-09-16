console.log("LOGIN SCRIPT IS RUNNING");

document.addEventListener("DOMContentLoaded", function () {

    const loginForm = document.getElementById("loginForm");
    const message = document.getElementById("message");

    loginForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        console.log("LOGIN BUTTON CLICKED");

        const email =
            document.getElementById("email").value.trim();

        const password =
            document.getElementById("password").value;


        // Check empty fields
        if (!email || !password) {

            message.textContent =
                "Please enter email and password.";

            message.style.color = "#dc2626";

            return;
        }


        // Show loading message
        message.textContent = "Logging in...";
        message.style.color = "#4f46e5";


        try {

            // Send login request
            const response = await fetch(
                "/api/users/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                }
            );


            const data = await response.json();


            console.log(
                "Login response:",
                response.status,
                data
            );


            // Successful login
            if (response.ok) {

                // Save JWT token
                localStorage.setItem(
                    "token",
                    data.token
                );


                // Check whether token was saved
                console.log(
                    "TOKEN SAVED:",
                    localStorage.getItem("token")
                );


                message.textContent =
                    "Login successful!";

                message.style.color = "#16a34a";


                // Open dashboard
                window.location.href =
                    "/dashboard.html";

            }


            // Login failed
            else {

                message.textContent =
                    data.message ||
                    "Invalid email or password.";

                message.style.color =
                    "#dc2626";
            }

        }


            // Connection/server error
        catch (error) {

            console.error(
                "Login error:",
                error
            );

            message.textContent =
                "Unable to connect to server.";

            message.style.color =
                "#dc2626";
        }

    });

});