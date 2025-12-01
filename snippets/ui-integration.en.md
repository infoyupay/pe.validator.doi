# UI Integration Examples
### Practical usage of `pe.validator.doi` with multiple UI toolkits

This document provides practical examples showing how to integrate this library with different graphical user interface frameworks, without introducing any external dependencies.  
The library remains minimalistic and UI-agnostic, but its use in forms and data-entry screens is common in accounting, tax, and ERP applications.

Included examples:

- JavaFX  
- Swing  
- SWT  
- QtJambi  
- Android (Kotlin/Java)

---

## 1. JavaFX

JavaFX cleanly separates the selected value (`valueProperty`) from the list of available items, which allows the UI to display values not present in the filtered list—without mutating the underlying catalog.

### Example: Filter DOI types by SUNAT usage context

```java
ComboBox<DoiType> doiCombo = new ComboBox<>();

UsageContext context = UsageContext.PLE;

List<DoiType> suitable = context.listSuitableDoi();

doiCombo.getItems().setAll(suitable);

// Loading an existing entity:
Entity entity = service.loadEntity();
doiCombo.setValue(entity.getDoiType());
```

### Notes:
- The ComboBox will display a selected value even if it is not part of the items list.  
- JavaFX **does not modify** your list when this happens.  
- This is ideal when loading legacy data or documents no longer permitted by current rules.

---

## 2. Swing (JComboBox)

Swing requires the selected value to exist inside the model.  
To display a value that is not part of the filtered list, you need to insert it manually.

```java
DefaultComboBoxModel<DoiType> model =
        new DefaultComboBoxModel<>(context.listSuitableDoi().toArray(new DoiType[0]));

JComboBox<DoiType> combo = new JComboBox<>(model);

DoiType loaded = entity.getDoiType();
model.addElement(loaded); // Swing needs this
combo.setSelectedItem(loaded);
```

### Alternative:
A custom renderer can show values outside the list without modifying the model.

---

## 3. SWT

SWT does not store arbitrary objects; it stores strings.  
A bi-directional mapping is required.

```java
Combo combo = new Combo(parent, SWT.READ_ONLY);

Map<String, DoiType> map = new HashMap<>();

for (DoiType dt : context.listSuitableDoi()) {
    combo.add(dt.getShortName());
    map.put(dt.getShortName(), dt);
}

// Selecting an existing entity:
combo.setText(entity.getDoiType().getShortName());
```

---

## 4. QtJambi

Qt widgets work similarly to SWT: they store text, with optional user data.

```java
QComboBox doiCombo = new QComboBox();

List<DoiType> options = context.listSuitableDoi();

for (DoiType dt : options) {
    doiCombo.addItem(dt.getShortName(), dt);
}

DoiType loaded = entity.getDoiType();
int index = options.indexOf(loaded);

if (index >= 0) {
    doiCombo.setCurrentIndex(index);
} else {
    doiCombo.setEditText(loaded.getShortName());
}
```

---

## 5. Android (Kotlin)

The standard `Spinner` widget uses `ArrayAdapter`.

```kotlin
val suitable = UsageContext.PLE.listSuitableDoi()

val adapter = ArrayAdapter(
    this,
    android.R.layout.simple_spinner_item,
    suitable.map { it.shortName }
)

spinner.adapter = adapter

// Load existing entity
val loaded = entity.doiType.shortName
val index = suitable.indexOfFirst { it.shortName == loaded }
spinner.setSelection(index.takeIf { it >= 0 } ?: 0)
```

### Notes:
- Android represents values as strings; you must map back to `DoiType` manually.

---

## Conclusion

- The library remains UI-independent but integrates seamlessly with all major Java UI toolkits.  
- JavaFX provides the cleanest experience for dynamic filtering and legacy value handling.  
- Swing and SWT require small adapters or mapping layers.  
- Android works naturally with adapters and Kotlin extensions.

