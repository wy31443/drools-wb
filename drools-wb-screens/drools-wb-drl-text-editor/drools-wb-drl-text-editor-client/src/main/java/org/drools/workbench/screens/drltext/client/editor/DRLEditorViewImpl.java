/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.drltext.client.editor;

import java.util.List;
import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.DSLSentence;
import org.drools.workbench.screens.drltext.client.resources.i18n.DRLTextEditorConstants;
import org.drools.workbench.screens.drltext.client.widget.ClickEvent;
import org.drools.workbench.screens.drltext.client.widget.DSLSentenceBrowserWidget;
import org.drools.workbench.screens.drltext.client.widget.DrlEditor;
import org.drools.workbench.screens.drltext.client.widget.FactTypeBrowserWidget;
import org.kie.workbench.common.widgets.metadata.client.KieEditorViewImpl;

public class DRLEditorViewImpl
        extends KieEditorViewImpl
        implements DRLEditorView {

    interface ViewBinder
            extends
            UiBinder<Widget, DRLEditorViewImpl> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    //Scroll-bar Height + Container padding * 2
    private static int SCROLL_BAR_SIZE = 32;
    private static int CONTAINER_PADDING = 15;
    private static int VERTICAL_MARGIN = SCROLL_BAR_SIZE + ( CONTAINER_PADDING * 2 );

    private DrlEditor drlEditor = null;
    private FactTypeBrowserWidget factTypeBrowser = null;
    private DSLSentenceBrowserWidget dslConditionsBrowser = null;
    private DSLSentenceBrowserWidget dslActionsBrowser = null;

    @UiField
    FlowPanel columnsContainer;

    @UiField
    FlowPanel columnBrowsers;

    @UiField
    FlowPanel columnDrl;

    @Override
    public void init( final DRLEditorPresenter presenter ) {
        this.factTypeBrowser.init( presenter );
    }

    @PostConstruct
    public void init() {
        this.drlEditor = new DrlEditor();

        final ClickEvent ce = new ClickEvent() {
            public void selected( String text ) {
                drlEditor.insertAtCursor( text );
            }
        };

        this.factTypeBrowser = new FactTypeBrowserWidget( ce );
        this.dslConditionsBrowser = new DSLSentenceBrowserWidget( ce,
                                                                  DRLTextEditorConstants.INSTANCE.showDSLConditions(),
                                                                  DRLTextEditorConstants.INSTANCE.dslConditions() );
        this.dslActionsBrowser = new DSLSentenceBrowserWidget( ce,
                                                               DRLTextEditorConstants.INSTANCE.showDSLActions(),
                                                               DRLTextEditorConstants.INSTANCE.dslActions() );
        initWidget( uiBinder.createAndBindUi( this ) );

        columnBrowsers.add( factTypeBrowser );
        columnBrowsers.add( dslConditionsBrowser );
        columnBrowsers.add( dslActionsBrowser );

        columnDrl.add( drlEditor );
    }

    @Override
    public void setContent( final String drl,
                            final List<String> fullyQualifiedClassNames ) {
        dslConditionsBrowser.setVisible( false );
        dslActionsBrowser.setVisible( false );
        factTypeBrowser.setFullyQualifiedClassNames( fullyQualifiedClassNames );
        factTypeBrowser.setDSLR( false );
        drlEditor.setText( drl );
    }

    @Override
    public void setContent( final String dslr,
                            final List<String> fullyQualifiedClassNames,
                            final List<DSLSentence> dslConditions,
                            final List<DSLSentence> dslActions ) {
        dslConditionsBrowser.setVisible( true );
        dslActionsBrowser.setVisible( true );
        factTypeBrowser.setFullyQualifiedClassNames( fullyQualifiedClassNames );
        factTypeBrowser.setDSLR( true );
        dslConditionsBrowser.setDSLSentences( dslConditions );
        dslActionsBrowser.setDSLSentences( dslActions );
        drlEditor.setText( dslr );
    }

    @Override
    public String getContent() {
        return drlEditor.getText();
    }

    @Override
    public void onResize() {
        final int height = getParent().getOffsetHeight() - VERTICAL_MARGIN;
        columnsContainer.setHeight( ( height > 0 ? height : 0 ) + "px" );
        drlEditor.onResize();
    }
}
