# pe.validator.doi

## 🌐 Languages

[🇪🇸 Español](README.md) · [🇬🇧 English](README.en.md)

---

## ⚡ TL;DR

`pe.validator.doi` is a minimal Java library that validates **Peruvian identity document types and numbers**:  
**DNI**, **RUC**, **CE**, and **Passport**.  
It only checks whether a given value **looks valid** according to its type.

---

## 📌 What is this module?

`pe.validator.doi` is a small Java utility library for validating the format of Peruvian identity documents (**DOI –
Documento Oficial de Identidad**).

Its goal is simple:

> Make sure a given value has the correct format before it enters any business logic.

It is:

- **Small & dependency-free**
- **Deterministic**
- **Compatible with Java 21, 23, 25+**
- **Designed for real Peruvian software environments**

It does *not* verify whether the document exists, nor does it query government services.  
It performs **format validation only**.

---

## ⚠️ Important Warning

The **RUC** validation performed by this library is **purely local**.  
It does **not** query SUNAT or any external government service, and it does **not** verify whether a taxpayer actually
exists.

A RUC may be considered “valid” simply because its **format** matches the expected structure,  
but this does **not** guarantee that the number exists or is assigned to any real person or business.

This library validates **format**, not existence.

---

## 🏛️ Who is SUNAT?

**SUNAT** is the *National Superintendency of Customs and Tax Administration* of Peru.  
It is the government authority responsible for:

- managing taxes,
- maintaining the official taxpayer registry (RUC),
- and overseeing tax and customs compliance.

In simple terms: SUNAT is Peru’s tax authority.  
If you need to verify whether a RUC **actually exists** or is **active**, that must be done through SUNAT’s official
services, not through this library.

This project does **not** connect to SUNAT or interact with any government system.

---

## 🇵🇪 Why does this project exist?

Peru uses multiple official identity document types (DNI, RUC, CE, Passport).  
Unfortunately, each public institution and private system implements its own validation rules — often incomplete or
inconsistent.

This library provides:

- a simple standard,
- consistent behavior,
- and reliable syntactic validation,

so developers can clean inputs **before** running any additional logic.

---

## 🚀 Installation

Available on Maven Central:

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.infoyupay:pe.validator.doi:<version>")
}
```

### Maven

```xml

<dependency>
    <groupId>com.infoyupay</groupId>
    <artifactId>pe.validator.doi</artifactId>
    <version><!-- version --></version>
</dependency>
```

---

## 🧪 Basic usage

```java
boolean ok = DoiValidator.isValid(DoiType.DNI, "12345678");
```

---

## 📘 Supported document types

| Type         | Description                       | PLAME Code | PLE Code | AFPNET Code | FV3800 Code | Regex           |
|--------------|-----------------------------------|------------|----------|-------------|-------------|-----------------|
| `OTHERS`     | Other documents                   | --         | 0        | --          | -           | \p{Alnum}{1,15} |
| `DNI`        | Peruvian National ID              | 01         | 1        | 0           | 01          | \d{8}           |
| `PNP`        | Military/Police ID                | 02         | 2        | 2           | -           | \p{Alnum}{1,15} |
| `CE`         | Foreigner ID Card                 | 04         | 4        | 1           | 04          | \p{Alnum}{1,12} |
| `RUC`        | Peruvian Taxpayer ID Number       | 06         | 6        | -           | 06          | \d{11}          |
| `PASSPORT`   | Peruvian or foreign passport      | 07         | 7        | 4           | 07          | \p{Alnum}{1,12} |
| `REFUGEE`    | Refugee ID Card                   | 09         | 0        | 9           | -           | \p{Alnum}{1,15} |
| `DIPLOMATIC` | Diplomatic Identity Card          | 22         | 0        | 7           | -           | \p{Alnum}{1,15} |
| `PTP`        | Temporary Residence Permit (PTP)  | 23         | 0        | 6           | -           | \p{Alnum}{1,15} |
| `ID`         | Foreign Identification            | 24         | 0        | 8           | 02          | \p{Alnum}{1,15} |
| `ID_PTP`     | PTP ID Card                       | 26         | 0        | 10          | -           | \p{Alnum}{1,15} |
| `TIN`        | Foreign Tax Identification Number | --         | 0        | -           | 01          | \p{Alnum}{1,15} |

### Special Case: RUC

The **RUC** (Registro Único de Contribuyentes) is Peru’s official taxpayer identification number.  
SUNAT (Peru’s tax authority) defines a verification model based on two elements:

1. The **first two digits** (the prefix)
2. A **check digit** calculated using a modulo-11 algorithm

For example, the RUC of Infoyupay is **20607854247**, which can be decomposed into three parts:

| Prefix | Identity Number | Check Digit |
|--------|-----------------|-------------|
| 20     | 60785424        | 7           |

#### Prefix rules

| Prefix | Meaning                                                                                    |
|--------|--------------------------------------------------------------------------------------------|
| 10     | Peruvian natural person                                                                    |
| 15     | Estates, marital societies, police and military personnel, and foreign individuals         |
| 16     | Mentioned as valid in SUNAT’s specification, but reserved (not publicly documented)        |
| 17     | Natural persons registered between 1993 and 2000                                           |
| 20     | Legal entities (including irregular companies, joint ventures, consortium contracts, etc.) |

#### Check digit rules

The check digit uses the **mod 11** algorithm established by SUNAT.  
The first 10 digits are multiplied by a positional weight and then summed.

Positional weights:

| Char index: | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|-------------|---|---|---|---|---|---|---|---|---|---|
| Weight:     | 5 | 4 | 3 | 2 | 7 | 6 | 5 | 4 | 3 | 2 |

Then:

1. Compute the weighted sum
2. Compute the remainder modulo 11
3. Compute the complement to 11
4. Adjust results 10 → 0 and 11 → 1
5. The result is the check digit

#### Practical example

| RUC:          | 2  | 0 | 6  | 0 | 7  | 8  | 5  | 4  | 2 | 4 | 7 |
|---------------|----|---|----|---|----|----|----|----|---|---|---|
| Weight:       | 5  | 4 | 3  | 2 | 7  | 6  | 5  | 4  | 3 | 2 | - |
| Partial prod: | 10 | 0 | 18 | 0 | 49 | 48 | 25 | 16 | 6 | 8 | - |

| Operation        | Result       |
|------------------|--------------|
| Partial sum      | 180          |
| Remainder        | 180 % 11 = 4 |
| Complement to 11 | 11 - 4 = 7   |
| Check digit      | 7            |

Since the RUC:

- begins with a valid prefix,
- has 11 digits,
- ends with **7**, and
- the calculated check digit is **7**,

then it is structurally valid.

**This does *not* mean the RUC exists — only that it is valid according to SUNAT’s verification algorithm.**

---

### Optimizations

At InfoYupay we love elegant optimizations. Instead of storing an array with the weight values, we use an arithmetic
approach to compute the weight during iteration.

The loop looks like this:

```java
for(int i = 0, x = 6;
i< 10;i++)
```

Before multiplying, we decrement `x`:

```java
sum +=(c -'0')*--x;
```

This yields the correct sequence:

- At index 0 → 5
- At index 1 → 4
- At index 2 → 3
- …

When we reach index 4 (where the weight restarts at 7), we reset x:

```java
if(i ==4)x =8;
```

This way, index 4 → 7, index 5 → 6, and so on until index 9 → 2.

This eliminates the need for an array, reduces memory pressure, and keeps this method with a negligible footprint even
in highly concurrent environments.

This is not premature micro-optimization — it avoids unnecessary storage when the pattern is deterministic and can be
expressed arithmetically.

---

## Value Sanitization

This library also provides utilities to “sanitize” values before running validation.  
We are not here to tell you how to do your job, nor to restrict the way you use this library.  
However, we would like to share our perspective on where data cleaning should happen — and where it should not.

At InfoYupay, we believe in **user freedom and ergonomics**. Every design decision we make aims to improve the
user’s workflow, not to force users to adapt to rigid internal standards.

For that reason, at the **physical model level** (i.e., the database), we do *not* sanitize the data.  
Users are completely free to enter the information **exactly as it appears on the original document**. What the user
types in the GUI is what gets stored in the database.

For example, if a passport number contains dashes or dots, the user may enter it exactly as shown — **as long as the
sanitized version of that input passes validation**.

```java
if (validate(doiType, sanitized(doiNumber))) {
    allowPersistence();
} else {
    showUserMessage("The value entered does not meet the validation requirements.");
}
```

But what happens later?  
What if the user enters an identification number full of dots, dashes, or formatting characters that systems like
SUNAT’s PLE or FV3800 *do not accept*?

The answer is simple:

```java
exportStream.write(sanitized(doiNumber));
```

This approach guarantees:

- **Traceability**:  
  The database stores the value exactly as it appears on the original document, making it easier to trace and audit.

- **Ergonomics**:  
  Users are less likely to make mistakes if they can simply copy the value as-is, without manually cleaning or
  reformatting it.

- **Safety**:  
  The sanitized export value is guaranteed to comply with SUNAT’s strict validation rules, preventing rejections,
  warnings, and painful audit surprises.

- **Separation of Responsibilities**:  
  Any robust, well-architected system must separate responsibilities correctly.  
  Sanitization should *never* depend on human users.  
  It should be a deterministic system behavior.

If you prefer to use this library differently, you absolutely can.  
Please don’t take this as a lecture — just friendly advice from someone who has already suffered through this path.

---

## 📄 License

Licensed under **GPLv3 or (at your option) any later version**.  
See [LICENSE.md](LICENSE.md) for details.

---

## 🤝 Contributions

Contributions are welcome.  
This module is part of the **InfoYupay** ecosystem, but is intended to be useful for any developer who needs to validate
Peruvian identifiers.

---

Made with ☕ and respect for the Peruvian software community.
