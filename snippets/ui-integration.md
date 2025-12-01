# Integración con Interfaces Gráficas
### Ejemplos prácticos usando `pe.validator.doi`

Este documento contiene ejemplos de integración entre esta biblioteca y diferentes frameworks de interfaz gráfica, sin agregar ningún tipo de dependencia externa.
La librería se mantiene minimalista, pero su uso en UI es común en sistemas tributarios, administrativos y ERP.

Los ejemplos incluyen:

- JavaFX
- Swing
- SWT
- QtJambi
- Android (Kotlin/Java)

---

## 1. JavaFX

JavaFX separa claramente el valor del combo (valueProperty) de la lista de opciones, lo que permite mostrar valores que no estén en la lista filtrada sin mutar el catálogo.

### Ejemplo: filtrar DOI por contexto SUNAT

```java
ComboBox<DoiType> doiCombo = new ComboBox<>();

UsageContext context = UsageContext.PLE;

List<DoiType> suitable = context.listSuitableDoi();

doiCombo.getItems().setAll(suitable);

Entity e = service.loadEntity();
doiCombo.setValue(e.getDoiType());
```

---

## 2. Swing (JComboBox)

Swing exige que el valor exista en el modelo.

```java
DefaultComboBoxModel<DoiType> model =
        new DefaultComboBoxModel<>(context.listSuitableDoi().toArray(new DoiType[0]));

JComboBox<DoiType> combo = new JComboBox<>(model);

DoiType loaded = entity.getDoiType();
model.addElement(loaded);
combo.setSelectedItem(loaded);
```

---

## 3. SWT

SWT no maneja objetos, solo cadenas.

```java
Combo combo = new Combo(parent, SWT.READ_ONLY);

Map<String, DoiType> map = new HashMap<>();

for (DoiType dt : context.listSuitableDoi()) {
    combo.add(dt.getShortName());
    map.put(dt.getShortName(), dt);
}

combo.setText(entity.getDoiType().getShortName());
```

---

## 4. QtJambi

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

```kotlin
val suitable = UsageContext.PLE.listSuitableDoi()

val adapter = ArrayAdapter(
    this,
    android.R.layout.simple_spinner_item,
    suitable.map { it.shortName }
)

spinner.adapter = adapter

val loaded = entity.doiType.shortName
val index = suitable.indexOfFirst { it.shortName == loaded }
spinner.setSelection(index.takeIf { it >= 0 } ?: 0)
```

---

## Conclusión

- La librería no incluye frameworks de UI, pero es totalmente compatible con todos ellos.
- JavaFX es el más flexible y limpio para integraciones tributarias.
- Swing, SWT y QtJambi requieren mapeos adicionales.
- Android funciona sin problemas con adaptadores.

