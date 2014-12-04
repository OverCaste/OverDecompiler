##Parsers
Parsing is defined in this program as a process to generate an easily printable class from raw binary data.

It involves steps such as reading bitmaps into enums, and the major step of translating instructions to MethodActions.

###MethodParsers
Method parsers are the nested parsers which convert instructions to MethodActions and MethodBlocks.

####MethodMembers
MethodMembers are any element of a program, for example:

```
int a = 0; //MethodActionSetVariable
if(a == 5) { //MethodBlockIf
    System.out.println(a); //MethodActionInvokeMethod
}
```

MethodBlocks contain an arbitrary amount of other MethodMembers, as well as a condition or other stateful information.

MethodActions are directly converted to text by a Printer by calling their getStringValue() method.

####Inlining
Some elements are automatically inlined, such as constants:

```
String s = "hello"
"hello".toString();
"hello".toString();
```

Would be converted to:

```
"hello".toString();
"hello".toString();
```

unless line number matching was specified.

Other elements are inlined if there are only one reference to them:

```
int a = 5;
System.out.println(a);
```

Would be converted to:

```
System.out.println(5);
```