/*
 * Copyright (C) 2018 Tran Le Duy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.duy.ide.javaide.editor.autocomplete.internal;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.duy.common.interfaces.Filter;
import com.duy.ide.code.api.SuggestItem;
import com.duy.ide.editor.internal.suggestion.Editor;
import com.duy.ide.javaide.editor.autocomplete.dex.JavaDexClassLoader;
import com.duy.ide.javaide.editor.autocomplete.model.ClassDescription;
import com.duy.ide.javaide.utils.DLog;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Complete class declaration
 * <p>
 * public? static? final? class Name (extends otherClass)? (implements otherInterfaces)?
 * public? enum Name
 * public? final? interface Name (extends otherInterfaces)?
 */
public class CompleteClassDeclared extends JavaCompleteMatcherImpl {
    //case: public class A
    //case: public class Lamborghini extends Car
    static final Pattern CLASS_DECLARE = Pattern.compile(
            //more modifiers, public static final ....
            "((public|protected|private|abstract|static|final|strictfp)\\s+)*" +
                    //type
                    "((class|inteface|enum)\\s+)" +
                    //name
                    "([a-zA-Z_][a-zA-Z0-9_]*)" +
                    //inherit
                    "(\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*))?" +
                    "(\\s+implements\\s+([a-zA-Z_][a-zA-Z0-9_]*))?");
    private static final Pattern END_EXTENDS
            = Pattern.compile("\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");
    private static final Pattern END_IMPLEMENTS
            = Pattern.compile("\\s+implements\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");

    private static final String TAG = "CompleteClassDeclared";
    private JavaDexClassLoader mClassLoader;

    public CompleteClassDeclared(JavaDexClassLoader classLoader) {

        mClassLoader = classLoader;
    }

    @Override
    public boolean process(Editor editor, String statement, ArrayList<SuggestItem> result) {
        Matcher matcher = CLASS_DECLARE.matcher(statement);
        if (matcher.find()) {
            matcher = END_IMPLEMENTS.matcher(statement);
            if (matcher.find()) {
                if (DLog.DEBUG) DLog.d(TAG, "process: END_IMPLEMENTS found");
                String incompleteInterface = matcher.group(1);
                getSuggestionInternal(editor, incompleteInterface, result,
                        "interface");
                return true;
            }

            matcher = END_EXTENDS.matcher(statement);
            if (matcher.find()) {
                if (DLog.DEBUG) DLog.d(TAG, "process: END_EXTENDS found");
                String incompleteInterface = matcher.group(1);
                getSuggestionInternal(editor, incompleteInterface, result,
                        "class");
                return true;
            }

        }
        return false;
    }

    /**
     * @param declareType - enum, class, annotation, interface
     */
    private void getSuggestionInternal(@NonNull Editor editor,
                                       @NonNull String incomplete,
                                       @NonNull List<SuggestItem> result,
                                       @Nullable String declareType) {
        Filter<Class> filter = null;
        switch (declareType) {
            case "interface":
                filter = new Filter<Class>() {
                    @Override
                    public boolean accepts(Class clazz) {
                        if (Modifier.isFinal(clazz.getModifiers())) {
                            return false;
                        }
                        // TODO: 11-Jun-18 package private can extends
                        if (!Modifier.isPublic(clazz.getModifiers())) {
                            return false;
                        }
                        return clazz.isInterface();
                    }
                };
                break;
            case "class":
                filter = new Filter<Class>() {
                    @Override
                    public boolean accepts(Class clazz) {
                        if (Modifier.isFinal(clazz.getModifiers())) {
                            return false;
                        }
                        // TODO: 11-Jun-18 package private can extends
                        if (!Modifier.isPublic(clazz.getModifiers())) {
                            return false;
                        }
                        return !clazz.isInterface();
                    }
                };
                break;
        }

        ArrayList<ClassDescription> classes = mClassLoader.findAllWithPrefix(incomplete, filter);
        setInfo(classes, editor, incomplete);
        result.addAll(classes);
    }

    @Override
    public void getSuggestion(Editor editor, String incomplete, List<SuggestItem> suggestItems) {
        getSuggestionInternal(editor, incomplete, suggestItems, null);
    }
}
