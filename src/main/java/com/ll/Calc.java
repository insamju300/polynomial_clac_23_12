package com.ll;

public class Calc {

  public static boolean recursionDebug = true;

  public static int runCallCount = 0;

  public static int run(String exp) { // 10 + (10 + 5)
    runCallCount++;

    exp = exp.trim(); //입력된 문장에서 좌우의 여백을 제거한다.
    exp = stripOuterBracket(exp); //문자열 양끝의 괄호를 모두 벗긴 문자열을 반환한다.

    if(isNegativeCaseBracket(exp)){
      exp=exp.substring(1) + " * -1";
    }

    if(recursionDebug){
      System.out.printf("exp(%d) : %s\n", runCallCount, exp);
    }


    // 연산기호가 없으면 바로 리턴
    if (!exp.contains(" ")) return Integer.parseInt(exp);

    boolean needToMultiply = exp.contains(" * "); //*연산자가 있을 경우의 컨디션 체크
    boolean needToPlus = exp.contains(" + ") || exp.contains(" - "); // +나 -연산자가 있을 경우의 컨디션 체크

    boolean needToCompound = needToMultiply && needToPlus; // 만약에 +와 *연산이 둘다 포함 된 경우의 컨디션 체크
    boolean needToSplit = exp.contains("(") || exp.contains(")"); //문에 괄호가 포함되어있을 경우의 컨디션 체크

    if (needToSplit) {  // 식에 괄호가 포함된다면

      int splitPointIndex = findSplitPointIndex(exp); //문자열을 잘라야할 기준이될 인덱스를 반환한다.


      String firstExp = exp.substring(0, splitPointIndex); //시작지점에서, 문자열을 자를 기준이될 연산자 직전까지의 문자열을 잘라서 firstExp변수에 담는다.
      String secondExp = exp.substring(splitPointIndex + 1); //문자열을 자를 기준이될 연산자 다은 글자부터, 마지막까지의 문자열을 secondExp 변수에 담는다.

      char operator = exp.charAt(splitPointIndex); //기준이될 연산자를 operator 변수에 담는다.

      exp = Calc.run(firstExp) + " " + operator + " " + Calc.run(secondExp); //연산자를 기준으로 나눠진 문자열을 각각 다시한번 이 메소드 안에 담아서 현재 메소드를 호출 한 후
                                                                             //메소드의 반환값과 기준점이되는 연산자를 결합한다.

      return Calc.run(exp); //27에서 결합한 문자열로 다시한번 현재 메서드를 호출한다.

    } else if (needToCompound) { //만약에 + 연산자와 *연산자가 복합적으로 존재하는 식인 경우
      String[] bits = exp.split(" \\+ "); //+연산자를 기준으로 식을 나눈다.

      return Integer.parseInt(bits[0]) + Calc.run(bits[1]); // TODO
    }
    if (needToPlus) {//만약에 *없이 +나 -연산자로만 이루어진 식이라면
      exp = exp.replaceAll("\\- ", "\\+ \\-"); //a - b가 있다면 a + -b 처럼 전체 문자열을 변경한다.

      String[] bits = exp.split(" \\+ "); // +를 기준으로 문자열을 분리한다.

      int sum = 0; //return 값을 저장할 변수

      for (int i = 0; i < bits.length; i++) { //+를 기준으로 나누어진 문자열 배열의 길이만큼 반복처리한다.
        sum += Integer.parseInt(bits[i]); //sum 변수에 +를 기준으로 나누어진 각 문자열들을 숫자로 변환하여 더한다.
      }

      return sum; //각 요소들이 전부 더해진 값을 반환한다.
    } else if (needToMultiply) {//만약에 *연산자로만 이루어진 식이라면
      String[] bits = exp.split(" \\* "); //* 연산자를 기준으로 문자열을 잘라 문자열 배열을 만든다.

      int rs = 1; //반환값을 저장할 변수다. 1인 이유는 곱셈 연산을 진행해야 하는데 초기값이 0이면 뭘 곱해도 0이니까

      for (int i = 0; i < bits.length; i++) { //문자열 배열의 길이만큼 반복한다.
        rs *= Integer.parseInt(bits[i]); //rs변수에 문자열 배열의 요소들을 숫자로 변환해 곱해준다.
      }
      return rs; // 요소들을 전부 곱해준 값을 반환한다.
    }

    throw new RuntimeException("처리할 수 있는 계산식이 아닙니다"); //상기 조건들에서 해결하지 못한 문자열의 경우 에러를 발생시켜 호출한 곳으로 던져준다.
  }

  private static boolean isNegativeCaseBracket(String exp) {
    if(exp.startsWith("-")==false){
      return false;
    }
    int bracketCount = 0;
    for(int i = 0; i < exp.length(); i++){
      if (exp.charAt(i)== '(') { //문자열의 i번째 문자가 (라면
        bracketCount++; //괄호의 깊이를 체크하는 변수의 값을 더해준다.
      } else if (exp.charAt(i)==')') {//문자열의 i번째 문자가 )라면
        bracketCount--; //괄호의 깊이를 체크하는 변수의 값을 빼준다.
      }

      if(bracketCount==0){
        if(exp.length() - 1 == i) return true;
      }
    }
    return false;
  }

  private static int findSplitPointIndexBy(String exp, char findChar) {
    int bracketCount = 0; //괄호 안에 있는경우 0보다 큰 수. 모든 괄호 바깥인 경우 0이 된다.

    for (int i = 0; i < exp.length(); i++) {//입력받은 문자열의 길이만큼 반복한다.
      char c = exp.charAt(i); // 문자열의 i번째 문자를 c에 담는다.

      if (c == '(') { //문자열의 i번째 문자가 (라면
        bracketCount++; //괄호의 깊이를 체크하는 변수의 값을 더해준다.
      } else if (c == ')') {//문자열의 i번째 문자가 )라면
        bracketCount--; //괄호의 깊이를 체크하는 변수의 값을 빼준다.
      } else if (c == findChar) {//문자열의 i번째 문자가 매개변수의 findChar이라면(현재 +와 * 문자가 들어올것이 가정된다.)
        if (bracketCount == 0) return i; //만약 괄호 안에 있는 상태가 아니라면, i의 값을 반환한다.
      }
    }
    return -1; //만약에 괄호 바깥에 입력받은 매개변수 문자(findChar)가 존재하지 않는 경우, -1을 반환한다.
  }

  private static int findSplitPointIndex(String exp) {
    int index = findSplitPointIndexBy(exp, '+'); //괄호 바깥쪽에 있는 + 문자의 위치를 index 변수에 담는다.

    if (index >= 0) return index; //만약 괄호 바깥쪽에 +문자가 존재하는 경우  그 값을 그대로 반환한다.

    return findSplitPointIndexBy(exp, '*'); // 그렇지 않다면 괄호 바깥쪽에 * 문자의 위치를 확인하여 확인되면 *의 위치를, 확인되지 않으면 -1을 반환한다.
  }

  private static String stripOuterBracket(String exp) {
    int outerBracketCount = 0; //반활할 문자열의 범위를 결정한 변수

    while (exp.charAt(outerBracketCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketCount) == ')') { //괄호가 여러겹일 때, 겹괄호가 끝나는 부분까지 반복한다.
      outerBracketCount++; // 92 주석 참조
    }

    if (outerBracketCount == 0) return exp; //만약에 괄호가 하나도 없다면 입력받은 문자열을 그대로 반환한다.


    return exp.substring(outerBracketCount, exp.length() - outerBracketCount); //겹괄호를 모두 벗긴 문자열을 반환한다.
  }
}