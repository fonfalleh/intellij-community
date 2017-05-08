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
package se.chalmers.dat261.model;

import com.intellij.codeInsight.documentation.InlineDocumentationAdapter;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.Action;


/**
 * Created by Jacob on 5/5/17.
 */
public class InlineDocumentationModel implements FsmModel {

  private enum State {Editor, Check, Fetch, Popup, Window}; // The different states of the model.
  private State currentState = State.Editor; // Initial state is in editor.
  private InlineDocumentationAdapter adapter;

  // Different kinds of elements that caret can be placed on in the editor.
  private enum editorElement {NoElem, Field, Method, Class, Package};
  // What kind of element that the caret currently points to.
  private editorElement caretElement;

  // Boolean controlling if documentation opens in a popup or window. True for window, false for popup. Default is popup.
  boolean window = false;

  // Boolean controlling if auto-update from source is enabled for window.
  private boolean autoUpdate = false;

  // Size of documentation history back/fwd stack.
  private int histBack = 0;
  private int histFwd = 0;

  // Current font size and it's limits.
  private int fontSize = 3;
  private final int MIN_FONTSIZE = 1;
  private final int MAX_FONTSIZE = 7;



  public InlineDocumentationModel() throws Exception {
    adapter = new InlineDocumentationAdapter();
  }

  @Override
  public Object getState() {
    return currentState;
  }

  @Override
  public void reset(boolean b) {
    currentState = State.Editor; //TODO from each state, make sure niceness.
  }

  public void cleanup() { // TODO implement
  }

  /**
   * The transitions with guards
   */

  @Action
  public void placeCaretNoElem() {
    adapter.placeCaretNoElem();
    if (currentState == State.Popup) {
      currentState = State.Editor;
      histBack = 0; // TODO Or when creating new doc?
      histFwd  = 0;
    }
    caretElement = editorElement.NoElem;
  }
  public boolean placeCaretNoElemGuard() {
    return currentState == State.Editor || currentState == State.Popup || currentState == State.Window;
  }
  @Action
  public void placeCaretField() { // TODO implement
    adapter.placeCaretField();
    caretElement = editorElement.Field;
  }
  public boolean placeCaretFieldGuard() {
    return currentState == State.Editor || currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void placeCaretMethod() { // TODO implement
    adapter.placeCaretMethod();
    caretElement = editorElement.Method;
  }
  public boolean placeCaretMethodGuard() {
    return currentState == State.Editor || currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void placeCaretClass() { // TODO implement
    adapter.placeCaretClass();
    caretElement = editorElement.Class;
  }
  public boolean placeCaretClassGuard() {
    return currentState == State.Editor || currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void placeCaretPackage() {
    adapter.placeCaretPackage();
    caretElement = editorElement.Package;
  }
  public boolean placeCaretPackageGuard() {
    return currentState == State.Editor || currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void checkCaretElement() {
    adapter.checkCaretElement();
    currentState = State.Check;
  }
  public boolean checkCaretElementGuard() {
    return currentState == State.Editor || currentState == State.Window;
  }

  @Action
  public void fetchDocumentation() {
    adapter.fetchDocumentation();
    currentState = State.Fetch;
  }
  public boolean fetchDocumentationGuard() {
    return currentState == State.Check;
  }

  @Action
  public void displayDocumentation() {
    adapter.displayDocumentation();
    if(window){
      currentState = State.Window;
    }
    else {
      currentState = State.Popup;
    }
  }
  public boolean displayDocumentationGuard() {
    return currentState == State.Fetch;
  }

  @Action
  public void openToolWindow() {
    adapter.openToolWindow();
    window = true;
    currentState = State.Window;
  }
  public boolean openToolWindowGuard() {
    return currentState == State.Popup;
  }

  @Action
  public void restorePopup() {
    adapter.restorePopup();
    window = false;
    currentState = State.Popup;
  }
  public boolean restorePopupGuard() {
    return currentState == State.Window;
  }

  @Action
  public void followLink() {
    adapter.followLink();
    histBack++;
    histFwd = 0;
  }
  public boolean followLinkGuard() {
    return currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void historyBack() {
    adapter.historyBack();
    histBack--;
    histFwd++;
  }
  public boolean historyBackGuard() {
    return (currentState == State.Popup || currentState == State.Window) && histBack > 0;
  }

  @Action
  public void historyFwd() {
    adapter.historyFwd();
    histFwd--;
    histBack++;

  }
  public boolean historyFwdGuard() {
    return (currentState == State.Popup || currentState == State.Window) && histFwd > 0;
  }

  @Action
  public void fontSizeUp() {
    adapter.fontSizeUp();
    fontSize++;
  }
  public boolean fontSizeUpGuard() {
    return (currentState == State.Popup || currentState == State.Window) && fontSize < MAX_FONTSIZE;
  }

  @Action
  public void fontSizeDown() {
    adapter.fontSizeDown();
    fontSize--;
  }
  public boolean fontSizeDownGuard() {
    return (currentState == State.Popup || currentState == State.Window) && fontSize > MIN_FONTSIZE;
  }


  @Action
  public void closeDoc() {
    adapter.closeDoc();
    histBack = 0;
    histFwd = 0;
    currentState = State.Editor;
  }
  public boolean closeDocGuard() {
    return currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void editInSource() {
    adapter.editInSource();
    if(currentState == State.Popup){
      currentState = State.Editor;
    }
    //TODO caret element update
  }
  public boolean editInSourceGuard() {
    return currentState == State.Popup || currentState == State.Window;
  }

  @Action
  public void toggleAutoUpdate() {
    adapter.toggleAutoUpdate();
    autoUpdate = !autoUpdate;
  }
  public boolean toggleAutoUpdateGuard() {
    return currentState == State.Window;
  }

}
