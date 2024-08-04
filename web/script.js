document.addEventListener('DOMContentLoaded', function () {
    const screen = document.getElementById('screen');
    const keys = document.querySelector('.calculator-keys');

    let currentInput = '';
    let firstValue = null;
    let operator = null;
    let shouldResetScreen = false;

    keys.addEventListener('click', function (event) {
        if (!event.target.matches('button')) return;

        const button = event.target;
        const value = button.value;

        if (button.classList.contains('operator')) {
            handleOperator(value);
            return;
        }

        if (button.classList.contains('decimal')) {
            handleDecimal();
            return;
        }

        if (button.classList.contains('all-clear')) {
            handleAllClear();
            return;
        }

        if (button.classList.contains('equal-sign')) {
            handleEqualSign();
            return;
        }

        handleNumber(value);
    });

    function handleNumber(number) {
        if (shouldResetScreen) {
            screen.value = number;
            shouldResetScreen = false;
        } else {
            screen.value = screen.value === '0' ? number : screen.value + number;
        }
        currentInput += number;
    }

    function handleOperator(nextOperator) {
        if (operator && shouldResetScreen) {
            operator = nextOperator;
            return;
        }

        if (firstValue == null) {
            firstValue = parseFloat(currentInput);
        } else if (operator) {
            const result = performCalculation(firstValue, parseFloat(currentInput), operator);
            screen.value = result;
            firstValue = result;
        }

        operator = nextOperator;
        currentInput = '';
        shouldResetScreen = true;
    }

    function handleDecimal() {
        if (shouldResetScreen) {
            screen.value = '0.';
            shouldResetScreen = false;
            return;
        }

        if (!screen.value.includes('.')) {
            screen.value += '.';
        }
    }

    function handleAllClear() {
        screen.value = '0';
        currentInput = '';
        firstValue = null;
        operator = null;
        shouldResetScreen = false;
    }

    function handleEqualSign() {
        if (operator && currentInput) {
            const result = performCalculation(firstValue, parseFloat(currentInput), operator);
            screen.value = result;
            firstValue = result;
            operator = null;
            currentInput = result;
            shouldResetScreen = true;
        }
    }
    function handleOperator(nextOperator) {
        if (nextOperator === 'sqrt') {
            screen.value = Math.sqrt(parseFloat(currentInput));
            firstValue = null;
            operator = null;
            currentInput = '';
            shouldResetScreen = true;
            return;
        }
    
    }

    function performCalculation(firstValue, secondValue, operator) {
        switch (operator) {
            case '+':
                return firstValue + secondValue;
            case '-':
                return firstValue - secondValue;
            case '*':
                return firstValue * secondValue;
            case '/':
                return firstValue / secondValue;
            default:
                return secondValue;
        }
    }

    


});
