/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.idea.codeInsight;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.InnerTestClasses;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@InnerTestClasses({JetInspectionTestGenerated.Intentions.class, JetInspectionTestGenerated.Inspections.class})
@RunWith(JUnit3RunnerWithInners.class)
public class JetInspectionTestGenerated extends AbstractJetInspectionTest {
    @TestMetadata("idea/testData/intentions")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Intentions extends AbstractJetInspectionTest {
        public void testAllFilesPresentInIntentions() throws Exception {
            JetTestUtils.assertAllTestsPresentInSingleGeneratedClass(this.getClass(), new File("idea/testData/intentions"), Pattern.compile("^(inspections\\.test)$"));
        }

        @TestMetadata("attributeCallReplacements/replaceGetIntention/inspectionData/inspections.test")
        public void testAttributeCallReplacements_replaceGetIntention_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/intentions/attributeCallReplacements/replaceGetIntention/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("branched/ifThenToElvis/inspectionData/inspections.test")
        public void testBranched_ifThenToElvis_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/intentions/branched/ifThenToElvis/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("branched/ifThenToSafeAccess/inspectionData/inspections.test")
        public void testBranched_ifThenToSafeAccess_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/intentions/branched/ifThenToSafeAccess/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("removeExplicitTypeArguments/inspectionData/inspections.test")
        public void testRemoveExplicitTypeArguments_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/intentions/removeExplicitTypeArguments/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("simplifyNegatedBinaryExpressionIntention/inspectionData/inspections.test")
        public void testSimplifyNegatedBinaryExpressionIntention_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/intentions/simplifyNegatedBinaryExpressionIntention/inspectionData/inspections.test");
            doTest(fileName);
        }
    }

    @TestMetadata("idea/testData/inspections")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Inspections extends AbstractJetInspectionTest {
        public void testAllFilesPresentInInspections() throws Exception {
            JetTestUtils.assertAllTestsPresentInSingleGeneratedClass(this.getClass(), new File("idea/testData/inspections"), Pattern.compile("^(inspections\\.test)$"));
        }

        @TestMetadata("unusedSymbol/class/inspectionData/inspections.test")
        public void testUnusedSymbol_class_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/class/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("unusedSymbol/function/inspectionData/inspections.test")
        public void testUnusedSymbol_function_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/function/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("unusedSymbol/object/inspectionData/inspections.test")
        public void testUnusedSymbol_object_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/object/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("unusedSymbol/parameter/inspectionData/inspections.test")
        public void testUnusedSymbol_parameter_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/parameter/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("unusedSymbol/property/inspectionData/inspections.test")
        public void testUnusedSymbol_property_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/property/inspectionData/inspections.test");
            doTest(fileName);
        }

        @TestMetadata("unusedSymbol/typeParameter/inspectionData/inspections.test")
        public void testUnusedSymbol_typeParameter_inspectionData_Inspections_test() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/inspections/unusedSymbol/typeParameter/inspectionData/inspections.test");
            doTest(fileName);
        }
    }
}
