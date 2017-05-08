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
package com.intellij.codeInsight.documentation;

import com.intellij.openapi.actionSystem.IdeActions;
import se.chalmers.dat261.adapter.BaseAdapter;

/**
 * Created by Jacob on 5/5/17.
 */
public class InlineDocumentationAdapter extends BaseAdapter {
  public InlineDocumentationAdapter() throws Exception {
    super("/model-based/InlineDocumentation.java");
    myDocumentationManager = DocumentationManager.getInstance(myEditor.getProject());
    System.out.println("Hello!");
    DocumentationComponent comp = new DocumentationComponent(myDocumentationManager);
    System.out.println("Hello!");
    myDocumentationManager.showJavaDocInfo(myEditor, myFile, false);

  }
  private DocumentationManager myDocumentationManager;

  public void placeCaretNoElem() {
    myEditor.getCaretModel().moveToOffset(0); //TODO TEST
    //TODO specific caret positions, from editor. Should be straightforward.
  }

  public void placeCaretField() {
  }

  public void placeCaretMethod() {
  }

  public void placeCaretClass() {
  }

  public void placeCaretPackage() {
  }

  public void checkCaretElement() {
    executeAction(IdeActions.ACTION_QUICK_JAVADOC); //TODO TEST
  }

  public void fetchDocumentation() { //Automatic?

  }

  public void displayDocumentation() { //Automatic?
    //myEditor.getProject().
    myEditor.getProject();
  }

  public void openToolWindow() {
    // Given component that is popup, display same info in window
  }

  public void restorePopup() {
    // And vice versa
  }

  public void followLink() {

  }

  public void historyBack() {
  }

  public void historyFwd() {
  }

  public void fontSizeUp() {
  }

  public void fontSizeDown() {
  }

  public void closeDoc() {
  }

  public void editInSource() {
  }

  public void toggleAutoUpdate() {
  }

  public String getName() {
    return "testInlineDocumentation";
  }

}
