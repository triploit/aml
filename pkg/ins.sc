func install
{
	sudo make all
	sudo chmod +x aml
	sudo mv od/aml.jar /usr/bin/AML.jar
	sudo mv aml /usr/bin/aml
	sudo rm -rf od
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
