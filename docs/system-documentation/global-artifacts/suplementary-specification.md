# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

> - (ii) User authentication to editor and player with password containing seven alphanumeric chars with 4 capital letters and 2 digits.

## Usability 

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

> - During the simulation execution current budget and date should be
    permanently visible.

## Reliability
 
_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

> - Validation of business rules must be respected when recording and updating data.

## Performance
_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

> - Nothing spcified.

## Supportability
_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

> - The code should allow an easy maintenance and addition of features.
> - The application documentation should be in English.
> - There should be a Javadoc from code documentation to ease support.

## +


### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

> - Should be developed using the Object-Oriented practices.
> - Dev Tools: Github and IntelliJ, JUnit 5 and JaCoCo.
> - All images regarding the software development process need to be in SVG format
> - The application ought to employ object serialization to guarantee the 
  persistence of the data in two successive runs.


### Implementation Constraints
 
_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

> - The program should be done in Java language
> - Adopt the coding standards camelCase.
> - The application’s graphical interface is to be developed in JavaFX 11
    covering the functionalities related with the Player

### Interface Constraints
_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

 > Nothing specified.


### Physical Constraints
_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._
 
 > Nothing specified.
