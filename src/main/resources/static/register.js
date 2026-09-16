console.log("REGISTER SCRIPT IS RUNNING");

document.addEventListener("DOMContentLoaded", function () {

    const registerForm =
        document.getElementById("registerForm");

    const registerMessage =
        document.getElementById("registerMessage");


    registerForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            const name =
                document.getElementById("name")
                    .value
                    .trim();

            const email =
                document.getElementById("email")
                    .value
                    .trim();

            const password =
                document.getElementById("password")
                    .value;


            if (!name || !email || !password) {

                registerMessage.textContent =
                    "Please fill in all fields.";

                registerMessage.style.color =
                    "#dc2626";

                return;
            }


            registerMessage.textContent =
                "Creating account...";

            registerMessage.style.color =
                "#4f46e5";


            try {

                const response =
                    await fetch(
                        "/api/users",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify({
                                    name: name,
                                    email: email,
                                    password: password
                                })
                        }
                    );


                const data =
                    await response.json();


                console.log(
                    "Registration response:",
                    response.status,
                    data
                );


                if (response.ok) {

                    registerMessage.textContent =
                        "Account created successfully!";

                    registerMessage.style.color =
                        "#16a34a";


                    setTimeout(
                        function () {

                            window.location.href =
                                "/index.html";

                        },
                        1200
                    );

                } else {

                    registerMessage.textContent =
                        data.message ||
                        "Unable to create account.";

                    registerMessage.style.color =
                        "#dc2626";

                }

            }

            catch (error) {

                console.error(
                    "Registration error:",
                    error
                );

                registerMessage.textContent =
                    "Unable to connect to server.";

                registerMessage.style.color =
                    "#dc2626";

            }

        }
    );

});