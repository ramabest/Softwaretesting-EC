document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("[data-confirm]").forEach((element) => {
    element.addEventListener("click", (event) => {
      if (!window.confirm(element.dataset.confirm)) {
        event.preventDefault();
      }
    });
  });

  const pinForm = document.querySelector("[data-pin-form]");
  if (!pinForm) {
    return;
  }

  const pinValue = pinForm.querySelector("[data-pin-value]");
  const dots = Array.from(pinForm.querySelectorAll("[data-pin-dot]"));

  const renderPin = () => {
    dots.forEach((dot, index) => {
      dot.classList.toggle("filled", index < pinValue.value.length);
    });
  };

  const appendDigit = (digit) => {
    if (pinValue.value.length < 4) {
      pinValue.value += digit;
      renderPin();
    }
  };

  pinForm.querySelectorAll("[data-pin-key]").forEach((button) => {
    button.addEventListener("click", () => appendDigit(button.dataset.pinKey));
  });

  pinForm.querySelector("[data-pin-back]").addEventListener("click", () => {
    pinValue.value = pinValue.value.slice(0, -1);
    renderPin();
  });

  pinForm.querySelector("[data-pin-clear]").addEventListener("click", () => {
    pinValue.value = "";
    renderPin();
  });

  document.addEventListener("keydown", (event) => {
    if (/^[0-9]$/.test(event.key)) {
      appendDigit(event.key);
    }
    if (event.key === "Backspace") {
      pinValue.value = pinValue.value.slice(0, -1);
      renderPin();
    }
  });

  pinForm.addEventListener("submit", (event) => {
    if (!/^[0-9]{4}$/.test(pinValue.value)) {
      event.preventDefault();
    }
  });
});
