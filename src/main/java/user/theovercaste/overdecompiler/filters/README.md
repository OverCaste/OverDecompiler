#Description
Filters are used to remove useless edge cases from the parsed [human readable] class.

They have diverse applications, from renaming variables to removing implicit code.

#Examples
##FilterRemoveImplicitConstructor
All java classes must have a single constructor. If you don't define one, then an implicit constructor is created in the bytecode.

This filter removes that constructor if it's the only one defined.