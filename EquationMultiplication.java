import java.util.Arrays;
import java.util.Scanner;

class Term 
{
    int coeff = 0;
    char[] variables;
    int[] powers;
    StringBuilder pattern = new StringBuilder();

    @Override
    public String toString() {
        return "Term [coeff=" + coeff + ", variable=" + Arrays.toString(variables) + ", power=" + Arrays.toString(powers)
                + "]" +"\nPattern = " + pattern + "\n";
    }
}

class Equation
{
    Term[] terms;

    Equation(int NOT)
    {
        terms = new Term[NOT];
        for(int i = 0; i < NOT; i++)
        {
            terms[i] = new Term();
        }
    }

    @Override
    public String toString() {
        return "Equation [terms=" + Arrays.toString(terms) + "]";
    }
}

public class EquationMultiplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("**************** EQUATION MULTIPLICATION ****************");
        System.out.println("Enter two equations (Eg : 2x^2 + y^4z^3 - 5xy - 0yz^2 + 0)");

        // String input1 = "3x^2 + 5y - x^2z^3 + 7 - 10x +2z^2x^4y";
        // String input1 = "x^2 + y^3";
        System.out.print("Equation 1 : ");
        String input1 = sc.nextLine();
        Equation e1 = generateEquation(input1);


        // String input2 = "-2x + 8y^3 -0xy -8z -0 +0";
        // String input2 = "4y^3 + 3z^1";
        System.out.print("Equation 2 : ");
        String input2 = sc.nextLine();
        Equation e2 = generateEquation(input2);

        // System.out.println(e1 + "\n" + e2 +"\n\n");

        //Multiplication of two equations e1 and e2
        Equation result = multiplyEquations(e1, e2);

        // System.out.print("Multiplication of ");
        // printEquation(e1);
        // System.out.print(" and ");
        // printEquation(e2);
        // System.out.print(" is ");
        // printEquation(result);

        System.out.print("Multiplication result : ");
        printEquation(result);

        sc.close();
    }

    private static Equation multiplyEquations(Equation e1, Equation e2) {

        int n = e1.terms.length;
        int m = e2.terms.length;

        Equation res = new Equation(n*m);
        int k = 0;

        for(int i = 0; i < n; i++)
        {
            if(e1.terms[i] == null){
                continue;
            }
            Term left = e1.terms[i];
            int v1 = 0;
            if(left.variables != null) 
            {
                v1 = left.variables.length;
            }
            for(int j = 0; j < m; j++)
            {
                if(e2.terms[j] == null){
                    continue;
                }
                Term right = e2.terms[j];
                res.terms[k].coeff = left.coeff * right.coeff;
                int v2 = 0;
                if(right.variables != null)
                {
                    v2 = right.variables.length;
                }
                StringBuilder varList = new StringBuilder();
                int noOfVar = 0;
                
                int l = 0, r = 0;

                while(l < v1 && r < v2)
                {
                    if(left.variables[l] == right.variables[r])
                    {
                        varList.append(left.variables[l]).append("^").append(left.powers[l]*right.powers[r]);
                        noOfVar++;
                        l++;
                        r++;
                    }
                    else if(left.variables[l] < right.variables[r])
                    {
                        varList.append(left.variables[l]).append("^").append(left.powers[l]);
                        noOfVar++;
                        l++;
                    }
                    else
                    {
                        varList.append(right.variables[r]).append("^").append(right.powers[r]);
                        noOfVar++;
                        r++;
                    }
                }

                while(l < v1)
                {
                    varList.append(left.variables[l]).append("^").append(left.powers[l]);
                    noOfVar++;
                    l++;
                }
                while(r < v2)
                {
                    varList.append(right.variables[r]).append("^").append(right.powers[r]);
                    noOfVar++;
                    r++;
                }
                
                int temp = 0;
                boolean patternFound = false;
                for(; temp < k; temp++)
                {
                    if(res.terms[temp].pattern.toString().equals(varList.toString()))
                    {
                        patternFound = true;
                        break;
                    }
                }

                if(patternFound)
                {
                    System.out.println("Found");
                    res.terms[temp].coeff += res.terms[k].coeff;
                    res.terms[k] = null;
                }
                else
                {
                    int varLen = varList.length();
                    res.terms[k].variables = new char[noOfVar];
                    res.terms[k].powers = new int[noOfVar];
                    res.terms[k].pattern = varList;

                    int index = 0;
                    for(int var = 0; var < varLen; var++)
                    {
                        char c = varList.charAt(var);
                        if(Character.isLetter(c))
                        {
                            res.terms[k].variables[index] = c;
                            int p = var+2;
                            while (p < varLen && Character.isDigit(varList.charAt(p))) {
                                res.terms[k].powers[index] = res.terms[k].powers[index] * 10 + (varList.charAt(p) - '0');
                                p++;
                            }
                            index++;
                            var = p-1;
                        }
                    }
                }
                k++;
            }
        }
        
        return res;
    }

    private static void printEquation(Equation e) {
        int n = e.terms.length;
        boolean isFirst = true;
        for(int i = 0; i < n; i++)
        {
            if(e.terms[i] != null)
            {
                int co = e.terms[i].coeff;
                if(isFirst)
                {
                    System.out.print(co > 0 ? co : (co < 0) ? "- " + (-co) : ""); 
                    isFirst = false;
                }
                else
                {
                    System.out.print(co > 0 ? "+ " + co : (co < 0) ? "- " + (-co) : "");
                }
                if(e.terms[i].variables != null)
                {
                    int m = e.terms[i].variables.length;
                    for(int j = 0; j < m; j++)
                    {
                        System.out.print(e.terms[i].variables[j] + "^" + e.terms[i].powers[j]);
                    }
                }
                System.out.print(" ");
            } 
        }
        // System.out.println();
    }

    private static Equation generateEquation(String input) {
        int n = input.length();
        int noOfTerms = 0;
        
        for(int i = 0; i < n; i++)
        {
            if(input.charAt(i) == '-' || input.charAt(i) == '+')
            {
                noOfTerms++;
            }
        }
        noOfTerms += input.charAt(0) == '-' ? 0 : 1;
        Equation res = new Equation(noOfTerms);

        int j = 0;
        for(int i = 0; i < noOfTerms; i++)
        {
            int sign = input.charAt(j) == '-' ? -1 : 1;
            if(sign == -1 || j != 0) {
                j++;
            }
            boolean isCoeffZero = false;
            StringBuilder var = new StringBuilder();
            StringBuilder pow = new StringBuilder();
            while (j < n && (input.charAt(j) != '+' && input.charAt(j) != '-') )
            {
                char c = input.charAt(j);
                if(Character.isDigit(c))
                {
                    if(c == '0')
                    {
                        isCoeffZero = true;
                    }
                    res.terms[i].coeff = res.terms[i].coeff * 10 + (c - '0');
                }
                else if(Character.isLetter(c))
                {
                    var.append(c);
                    if(j == n-1 || input.charAt(j+1) != '^')
                    {
                        pow.append("1 ");
                    }
                    // res.terms[i].pattern.append(c);
                }
                else if(c == '^')
                {
                    // res.terms[i].pattern.append("^");
                    int p = j+1;

                    while(p < n && Character.isDigit(input.charAt(p)))
                    {
                        // res.terms[i].pattern.append(input.charAt(p));
                        pow.append(input.charAt(p));
                        p++;
                    }
                    pow.append(" ");
                    j = p-1;
                }
                j++;
            }
            res.terms[i].coeff *= sign;
            if(var.length() > 0)
            {
                res.terms[i].variables = var.toString().toCharArray();
            }
            if(pow.length() > 0)
            {
                String[] power = pow.toString().split(" ");
                int pCount = power.length;
                res.terms[i].powers = new int[pCount];
                for(int k = 0; k < pCount; k++)
                {
                    res.terms[i].powers[k] = Integer.parseInt(power[k].trim());
                }
            }

            if(isCoeffZero)
            {
                res.terms[i] = null;
            }
            else if(res.terms[i].coeff == 0 && var.length() > 0)
            {
                res.terms[i].coeff += 1 * sign;
                // else
                // {
                //     res.terms[i] = null;
                // }
            }
            if(res.terms[i] != null && res.terms[i].variables != null)
            {
                quickSortTerm(res.terms[i], 0, res.terms[i].variables.length-1);
                generatePattern(res.terms[i]);
            }
        }

        return res;
    }

    private static void generatePattern(Term term) {
        int n = term.variables.length;

        for(int i = 0; i < n; i++)
        {
            term.pattern.append(term.variables[i]).append("^").append(term.powers[i]);
        }
    }

    private static void quickSortTerm(Term term, int low, int high) {
        if(low < high)
        {
            int pivot = quickSort(term, low, high);
            quickSortTerm(term, low, pivot-1);
            quickSortTerm(term, pivot+1, high);
        }
    }

    private static int quickSort(Term term, int low, int high) {

        int pivot = high;
        int i = low-1;

        for(int j = low; j <= high; j++)
        {
            if(term.variables[pivot] > term.variables[j])
            {
                i++;
                swapChar(term.variables, i, j);
                swapInt(term.powers, i, j);
            }
        }

        i++;
        swapChar(term.variables, pivot, i);
        swapInt(term.powers, pivot, i);

        return i;
    }

    private static void swapChar(char[] arr, int i, int j) {
        arr[j] = (char) (arr[i] + arr[j] - ((char)(arr[i] = arr[j])));
    }
    private static void swapInt(int[] arr, int i, int j) {
        arr[j] = (arr[i] + arr[j] - (arr[i] = arr[j]));
    }
}