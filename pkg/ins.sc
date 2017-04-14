func install
{
	sudo chmod +x aml
	sudo mv out/artifacts/AML_jar/AML.jar /usr/bin/AML.jar
	sudo mv aml /usr/bin/aml
}

func remove
{
	sudo rm /usr/bin/AML.jar
	sudo rm /usr/bin/aml
}

func update
{
	&install
}
