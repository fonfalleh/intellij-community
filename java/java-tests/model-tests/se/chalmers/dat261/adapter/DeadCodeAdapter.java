/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.chalmers.dat261.adapter;

import com.intellij.codeInsight.daemon.impl.quickfix.SimplifyBooleanExpressionFix;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.util.PsiTreeUtil;

public class DeadCodeAdapter extends BaseAdapter {
  public DeadCodeAdapter() throws Exception {
    super("/model-based/DeadCode.java");
  }

  public void addDeadCode() {
    type("if(a != 2) { System.out.println(\"He's dead, Jim!\"); }\n");
  }

  public void rmDeadCode() {
    up();
    right();
    right();
    right();

    final int offset = getEditor().getCaretModel().getOffset();
    final PsiElement psiElement = getFile().findElementAt(offset).getParent().getParent();

    if (psiElement == null) return;
    final SimplifyBooleanExpressionFix fix = createIntention(psiElement);
    if (fix == null) return;

    CommandProcessor.getInstance().executeCommand(getProject(), () -> {
      PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
      WriteAction.run(fix::applyFix);
    }, "Some name", null);
  }

  private static SimplifyBooleanExpressionFix createIntention(PsiElement element) {
    if (!(element instanceof PsiExpression)) return null;
    if (PsiTreeUtil.findChildOfType(element, PsiAssignmentExpression.class) != null) return null;

    final PsiExpression expression = (PsiExpression)element;
    while (element.getParent() instanceof PsiExpression) {
      element = element.getParent();
    }
    final SimplifyBooleanExpressionFix fix = new SimplifyBooleanExpressionFix(expression, false);
    // simplify intention already active
    if (!fix.isAvailable() ||
        SimplifyBooleanExpressionFix.canBeSimplified((PsiExpression)element)) {
      return null;
    }
    return fix;
  }

  public String getDocumentText() {
    return "";
  }
}
