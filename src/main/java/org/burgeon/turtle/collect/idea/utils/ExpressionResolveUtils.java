package org.burgeon.turtle.collect.idea.utils;

import com.intellij.psi.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表达式解析工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/1
 */
public class ExpressionResolveUtils {

    private static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 将表达式解析为字符串
     *
     * @param psiExpression
     * @return
     */
    public static Object resolve(PsiExpression psiExpression) {
        if (psiExpression == null) {
            return null;
        }
        if (psiExpression instanceof PsiPolyadicExpression) {
            // 多元表达式
            PsiPolyadicExpression psiPolyadicExpression = (PsiPolyadicExpression) psiExpression;
            PsiExpression[] psiExpressions = psiPolyadicExpression.getOperands();
            List<Object> args = Arrays.stream(psiExpressions).map(expression -> resolve(expression))
                    .collect(Collectors.toList());
            return plus(args.toArray(new Object[args.size()]));
        } else if (psiExpression instanceof PsiBinaryExpression) {
            // 二元表达式
            PsiBinaryExpression psiBinaryExpression = (PsiBinaryExpression) psiExpression;
            Object lVal = resolve(psiBinaryExpression.getLOperand());
            Object rVal = resolve(psiBinaryExpression.getROperand());
            return plus(lVal, rVal);
        } else if (psiExpression instanceof PsiParenthesizedExpression) {
            // 带圆括号表达式
            PsiParenthesizedExpression psiParenthesizedExpression = (PsiParenthesizedExpression) psiExpression;
            return resolve(psiParenthesizedExpression.getExpression());
        } else if (psiExpression instanceof PsiReferenceExpression) {
            // 查询表达式
            PsiReferenceExpression psiReferenceExpression = (PsiReferenceExpression) psiExpression;
            PsiElement psiElement = psiReferenceExpression.resolve();
            if (psiElement == null) {
                return null;
            }
            PsiExpression expression = ((PsiField) psiElement).getInitializer();
            return resolve(expression);
        } else if (psiExpression instanceof PsiLiteralExpression) {
            // 字面量表达式
            PsiLiteralExpression psiLiteralExpression = (PsiLiteralExpression) psiExpression;
            return psiLiteralExpression.getValue();
        } else if (psiExpression instanceof PsiClassObjectAccessExpression) {
            // 类表达式
            PsiClassObjectAccessExpression psiClassObjectAccessExpression
                    = (PsiClassObjectAccessExpression) psiExpression;
            return psiClassObjectAccessExpression.getText();
        }
        throw new RuntimeException("Unexpected expression: " + psiExpression.getClass().getName());
    }

    /**
     * 将全部参数相加，返回相加的结果
     *
     * @param args
     * @return
     */
    private static Object plus(Object... args) {
        boolean isAllNumber = true;
        for (Object arg : args) {
            if (!(arg instanceof Number)) {
                isAllNumber = false;
            }
        }

        if (isAllNumber) {
            String script = "";
            for (Object arg : args) {
                script += arg + "+";
            }
            script = script.substring(0, script.length() - 1);
            try {
                return jse.eval(script);
            } catch (ScriptException e) {
                throw new RuntimeException("Unexpected script: " + script);
            }
        } else {
            String result = "";
            for (Object arg : args) {
                result += arg;
            }
            return result;
        }
    }

}
