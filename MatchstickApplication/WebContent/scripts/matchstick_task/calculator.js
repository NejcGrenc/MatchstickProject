
function Calculator(equation)
{
    this.equation = equation;
}


/* Sequences */
var numerals = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
var operators = ["+", "-", "*", "/"];
var comparator = "=";

function isNumeral(symbol)
{
    return numerals.indexOf(symbol) >= 0;
}
function isOperator(symbol)
{
    return operators.indexOf(symbol) >= 0;
}
function isComparator(symbol)
{
    return comparator === symbol;
}


Calculator.prototype.isValid = function() 
{
    // All elements must be valid
    // so no "?" are allowed
    if (this.equation.indexOf("?") !== -1)
        return false;
    
    var numberOfComparators = 0;
    for (var i in this.equation)
    {
        var symbol = this.equation[i];
        if (isNumeral(symbol)) 
        {
            /* Continue */
        }
        else if (isOperator(symbol))
        {
            // Each operator must be surrounded by two numerals
            if (isNumeral(this.equation[i-1]) && isNumeral(this.equation[i+1]))
            {
                console.log("ERROR, operator not surrounded by numerals", this.equation);
                return false;
            }
        }
        else if (isComparator(symbol))
        {
            numberOfComparators++;
            
            // Comparator must be surrounded by two numerals
            if (isNumeral(this.equation[i-1]) && isNumeral(this.equation[i+1]))
            {
                console.log("ERROR, comparator not surrounded by numerals", this.equation);
                return false;
            }
        }
    }
    
    if (numberOfComparators !== 1)
    {
        console.log("ERROR, too many or too few comparators observed in", this.equation);
        return false;
    }
    
    return true;
};


Calculator.prototype.isCorrect = function() 
{
    function Node(element)
    {
        this.element = element;
        this.leftChild;
        this.rightChild;
    }
    
    // Tree creation
    
    function createTree(subEquation)
    {
        // Comparator node
        var comparatorIndex = subEquation.indexOf(comparator);
        if (comparatorIndex >= 0)
        {
            return splitAndMakeNode(comparatorIndex, subEquation);
        }
        
        // Plus node (priority 1)
        var plusIndex = subEquation.indexOf("+");
        if (plusIndex >= 0)
        {
            return splitAndMakeNode(plusIndex, subEquation);
        }
        
        // Minus node (priority 2)
        var minusIndex = subEquation.indexOf("-");
        if (minusIndex >= 0)
        {
            return splitAndMakeNode(minusIndex, subEquation);
        }
        
        // Multiplication node (priority 3)
        var multiplicationIndex = subEquation.indexOf("*");
        if (multiplicationIndex >= 0)
        {
            return splitAndMakeNode(multiplicationIndex, subEquation);
        }
        
        // Minus node (priority 4)
        var divisionIndex = subEquation.indexOf("/");
        if (divisionIndex >= 0)
        {
            return splitAndMakeNode(divisionIndex, subEquation);
        }
        
        // Numeral node
        if (isNumeral(subEquation))
        {
            var node = new Node(subEquation);
            node.leftChild = null;
            node.rightChild = null;
            return node;
        }
        else
            console.log("Expected numeral, but got", subEquation);
    }
    
    function splitAndMakeNode(elementIndex, subEquation)
    {
        var element = subEquation[elementIndex];
        var leftPart = subEquation.substring(0, elementIndex);
        var rightPart = subEquation.substring(elementIndex + 1);

        var node = new Node(element);
        node.leftChild = createTree(leftPart);
        node.rightChild = createTree(rightPart);
        return node;
    }

    // Tree calculation
    
    function calculateTree(node)
    {
        // Comparator node
        if (isComparator(node.element))
        {
            return calculateTree(node.leftChild) === calculateTree(node.rightChild);
        }
        
        // Operator node
        if (isOperator(node.element))
        {
            var left = calculateTree(node.leftChild);
            var right = calculateTree(node.rightChild);
                
            // Addition
            if (node.element === "+")
            {
                return left + right;
            }
            
            // Subtraction
            if (node.element === "-")
            {
                return left - right;
            }
            
            // Multiplication
            if (node.element === "*")
            {
                return left * right;
            }
            
            // Division
            if (node.element === "/")
            {
                // We do not have to worry about division by 0,
                // as JavaScript supports infinity "value"
                return left / right;
            }
        }
        
        // Numeral node
        if (isNumeral(node.element))
        {
            return parseInt(node.element);
        }
    }
    
    
    var tree = createTree(this.equation);
    return calculateTree(tree);
};

//function testEquation()
//{
//    var eqStr = "2+5/0=7";
//    var calc = new Calculator(eqStr);
//    console.log(calc.isValid());
//    var topNode = calc.isCorrect();
//    console.log(topNode);
//}
