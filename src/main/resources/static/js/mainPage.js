function openModal() {
    document.getElementById("writeModal").style.display = "block";
    document.getElementById("modalOverlay").style.display = "block";
}

function closeModal() {
    document.getElementById("writeModal").style.display = "none";
    document.getElementById("modalOverlay").style.display = "none";
}

window.addEventListener('pageshow', function (event) {
    if (event.persisted || (window.performance && performance.getEntriesByType(
        "navigation")[0].type === "back_forward")) {
        window.location.href = window.location.pathname + "?refresh="
            + new Date().getTime();
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const editor = document.getElementById("htmlEditor");
    const hiddenTextarea = document.getElementById("content");

    document.querySelectorAll(".toolbar button").forEach(button => {
        button.addEventListener("click", () => {
            const command = button.dataset.command;
            const value = button.dataset.value || null;

            if (command === "createLink") {
                const url = prompt("링크 주소를 입력하세요:");
                if (url) {
                    document.execCommand(command, false, url);
                }
            } else {
                document.execCommand(command, false, value);
            }

            editor.focus();
        });
    });

    document.getElementById("writeForm").addEventListener("submit",
        function () {
            hiddenTextarea.value = editor.innerHTML;
        });
});

