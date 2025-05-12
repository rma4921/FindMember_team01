const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const confirmPasswordInput = document.getElementById("confirmPassword");
const submitBtn = document.getElementById("submitBtn");
const emailError = document.getElementById("emailError");

const rule1 = document.getElementById("rule1");
const rule2 = document.getElementById("rule2");
const rule3 = document.getElementById("rule3");

let isEmailValid = false;

function validateEmail() {
    const email = emailInput.value;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    isEmailValid = emailRegex.test(email);

    if (!isEmailValid) {
        emailError.style.display = "block";
    } else {
        emailError.style.display = "none";
    }

    updateSubmitState(isEmailValid);
}

function validatePassword() {
    const password = passwordInput.value;

    const hasLetter = /[a-zA-Z]/.test(password);
    const hasDigit = /[0-9]/.test(password);
    const hasSpecial = /[^a-zA-Z0-9]/.test(password);
    const typeCount = [hasLetter, hasDigit, hasSpecial].filter(Boolean).length;
    updateRule(rule1, typeCount >= 2);

    const noSpace = !/\s/.test(password);
    updateRule(rule2, password.length >= 8 && password.length <= 32 && noSpace);

    const noThreeSame = password.length >= 3 && !/(.)\1\1/.test(password);
    updateRule(rule3, noThreeSame);

    updateSubmitState();
}

function updateRule(element, isValid) {
    element.style.color = isValid ? "black" : "#666666";
}

function updateSubmitState() {
    const allValid =
        rule1.style.color === "black" &&
        rule2.style.color === "black" &&
        passwordInput.value === confirmPasswordInput.value &&
        isEmailValid;

    submitBtn.disabled = !allValid;
}

emailInput.addEventListener("input", validateEmail);
passwordInput.addEventListener("input", validatePassword);
confirmPasswordInput.addEventListener("input", updateSubmitState);