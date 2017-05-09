divert(-1)
dnl um4l: horrible uml with m4 and graphviz
dnl jashankj@cse.unsw.edu.au

define(`UMLBegin', `digraph "uml" {
    rankdir=TB;
    node [fontname="Roboto", shape="record"];
    edge [fontname="Roboto"];
')
define(`UMLEnd', `}')
define(`PublField', ``+ $1: $2'')
define(`ProtField', ``# $1: $2'')
define(`Field', ``– $1: $2'')

define(`Constructor', ``+ «constructor»($1)'')
define(`Method', ``+ $1($2): $3'')
define(`ProtMethod', ``# $1($2): $3'')
define(`PrivMethod', ``– $1($2): $3'')

define(`_ClassInner',
`ifelse(`$#', `0', `',
        `$#', `1', ``$1\l'',
        ``$1\l'_ClassInner(shift($@))')')

define(`Interface', ``$1 [label="{«interface»\n\N\n|'_ClassInner($2)`}"]'')
define(`Enum', ``$1 [label="{«enumeration»\n\N\n|'_ClassInner($2)`}"]'')
define(`Class', ``$1 [label="{\N\n|'_ClassInner($2)`|'_ClassInner($3)`}"]'')
define(`AssociatedWith', `$1 -> $2 [arrowhead="open", style="solid"]')
define(`DependsOn', `$1 -> $2 [arrowhead="open", style="dashed"]')
define(`Aggregates', `$1 -> $2 [arrowtail="ediamond", arrowhead="none", dir="back", style="solid"]')
define(`Composes', `$1 -> $2 [arrowtail="diamond", arrowhead="none", dir="back", style="solid"]')
define(`Extends', `$2 -> $1 [arrowtail="empty", arrowhead="none", dir="back", style="solid"]')
define(`Implements', `$2 -> $1 [arrowtail="empty", arrowhead="none", dir="back", style="dashed"]')
divert(1)dnl
