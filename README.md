# AML
Another markup language.

## Installation - EN/US
AML is realy easy to install. With SecPM you can install the package with:

    sudo secpm install aml

Um AML ohne SecPM zu installieren, einfach folgende Dateien verschieben (Linux):
To install AML without SecPM, move ...

* aml to /usr/bin/
* out/artifacts/AML_jar/AML.jar to /usr/bin/  

Then give aml execution permissions:

    sudo chmod +x /usr/bin/aml

AML is now callable from the terminal.

For Windows, move ...
* aml.bat to C:\Windows\System32 verschieben
* out/artifacts/AML_jar/AML.jar to C:\Windows\System32

That's it! AML is now callable from the command line.

## Installation - DE
AML ist recht einfach zu installieren, mit SecPM könnt ihr einfach das Package "aml" installieren.

    sudo secpm install aml

Um AML ohne SecPM zu installieren, einfach folgende Dateien verschieben (Linux):
* aml nach /usr/bin/
* out/artifacts/AML_jar/AML.jar nach /usr/bin/  

Danach macht ihr aml ausführbar:

    sudo chmod +x /usr/bin/aml

Ab jetzt kann man AML von der Konsole mit "aml" aufrufen.

Für Windows:
* aml.bat nach C:\Windows\System32 verschieben
* out/artifacts/AML_jar/AML.jar nach C:\Windows\System32 verschieben

Das wars! "aml" kann man nun von der Konsole aus aufrufen.

## Hello-World in AML and HTML

```go
html
{
    head
    {
        style
        {
            div.container {
                color: green;
                font-size: 20px;
            }
        }
    }

    body
    {
        script
        {
            for (i = 1;i <= 10;i++)
            {
                alert("Hello: "+i+"/10");
            }
        }

        div (class: "container")
        {
            "Hello World\nHi!\n"
            "d"
        }
    }
}
```
    
That will produce this HTML code:

```html
    <html>

        <head>

            <style>			
                div.container {
                    color: green;
                    font-size: 20px;
                }
            </style>

        </head>

        <body>

            <script>			
                for (i = 1;i <= 10;i++)
                {
                    alert("Hello: "+i+"/10");
                }
            </script>

            <div class="container">

    Hello World<br>Hi!<br>d
            </div>

        </body>

    </html>
```

