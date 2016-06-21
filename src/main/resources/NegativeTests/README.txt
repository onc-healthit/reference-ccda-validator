--------------------------------------------------------------------------------------------
File Name="Inpatient_CodeNot_in_ValueSet.xml"

*Allergies Section*

Should throw error at Line: 550
code="485-2"
  Not in  codeSystem="2.16.840.1.113883.6.1"

/component/section/code[@code]


*Care Plan section*

Should throw error at Line: 2156
code="1876-5"
 Not in  codeSystem="2.16.840.1.113883.6.1"

--------------------------------------------------------------------------------------------
File Name="Inpatient_PooprlyFormed.xml"

Should show an error at Line: 94
missing end tag : </birthplace>


--------------------------------------------------------------------------------------------
File Name="Inpatient_wrongTemplateIds.xml"

*Immunizations*

Should show error at Line:1221
Wrong templateId

*Results Section*

Should show error at Line:3067
Wrong templateId
component/section/entry/organizer/component/observation/templateId[@root]
--------------------------------------------------------------------------------------------
File Name="Inpatient_missingTemplateIds.xml"

*Medications section*

Should show error at Line: 1418
Missing templateId

*Problems Section*

Should show error at Line: 2413
Missing templateId


--------------------------------------------------------------------------------------------
File Name="Ambulatory_missingNarrative.xml"

*Problems Section*
Should show error at Line: 2308
Missing Narrative

--------------------------------------------------------------------------------------------
File Name="Ambulatory_InvalidCD.xml"

*Allergies Section*
Should show error at Line: 551
Wrong CodeSystem

*Encounters Section*
Should show error at Line: 915
Missing codeSystem


--------------------------------------------------------------------------------------------
File Name="Ambulatory_invalidDataTypes.xml"

ClinicalDocument/author/time

Should show error at Line: 120
time
 value="abcdefg" is invalid

*Allergies Section*

component/section/entry/act/entryRelationship/observation/value/[@xsi:type]
Should show error at Line: 630
xsi:type="akjhdjkash" not defined type



--------------------------------------------------------------------------------------------
File Name="Ambulatory_missingMU@elements.xml"

ClinicalDocument/recordTarget/patientRole/patient
Should show error at Line: 49
Missing <birthTime>

*Section Vital Signs*

content/section/entry/organizer/component
Should show error at line: 3333
Missing observation (Height)

--------------------------------------------------------------------------------------------
File Name="Inpatient_IncorrectMedication.xml"

*Medications section*

Should show error for missing narative

Should show error at Line: 1430
missing <id> for entry

entry/substanceAdministration/routeCode
invalid codeSystem

entry/substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial
missing MU2 data element manufacturedMaterial/code which describes the drug.

--------------------------------------------------------------------------------------------
File Name= "Inpatient_IncorrectProblems.xml"

Should show error at line: 2300
invalid templateId


should show error for missing narative


should show error at line : 2384
incomplete problem observation template (missing 'value')

should show error at line: 2435
Illegal classCode value for status observation template



--------------------------------------------------------------------------------------------
File Name="Inpatient_IncorrectAllergies.xml"

Should show error at line: 603
for time value = null

Missing narrative for second allergy

Allergy #1 reaction observation template not coded



--------------------------------------------------------------------------------------------
File Name="Ambulatory_IncorrectLabResults.xml"

Should show error at line: 2932
Incomplete narrative

Should show error at line: 2941
Missing classCode

Should show error at line: 2954
Invalid code value

Should show error at line: 3006
templateID not available for result#2

--------------------------------------------------------------------------------------------
File Name="Ambulatory_IncorrectProcedures.xml"

Should show error at line: 2662
code value for procedure is not entered

Should show error at line: 2686
performer information is not coded

Should show error at line: 2692
participant information not coded correctly (missing @root for templateId)

--------------------------------------------------------------------------------------------
File Name="Ambulatory_IncorrectVitalSigns.xml"

Should show error at line: 3499
Missing observation template

Should show error at line: 3307
<content> tag not coded for a vital sign in narrative

Should show error at line: 3337
template is not wellformed
Misplaced <interpretationCode> tag

--------------------------------------------------------------------------------------------
File Name="Ambulatory_IncorrectImmunization.xml"

Should show error at line: 1281
wrong codeSystem for routeCode
substanceAdministration/routeCode[@codeSystem]

Should show error at line: 1225
code value not present in codeSystem

Should show error at line: 1323
displayName not coded in Instruction template
entry/substanceAdministration/entryRelationship/act/code[@displayName]

