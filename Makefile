SOURCES = $(shell ls *.scala)
S = scala
SC = scalac
TARGET = target
CP = $(TARGET):scalatest.jar
SPEC = scala.RomanSpec
TEMP_DIR ?= _LATEX

all: pdf

compile: $(SOURCES)
	@echo "Compiling $(SOURCES)..."
	@$(SC) -cp . -d $(TARGET) $(SOURCES)

Parallel.class: $(SOURCES)

run: Parallel.class
	@scala -cp $(TARGET) Parallel

test: compile
	@$(S) -cp $(CP) org.scalatest.tools.Runner -p . -o -s $(SPEC)

pdf:
	@mkdir -p $(TEMP_DIR)
	xelatex -output-directory ./$(TEMP_DIR) spark1.tex
	xelatex -output-directory ./$(TEMP_DIR) spark2.tex
	cp $(TEMP_DIR)/*.pdf .

clean:
	@$(RM) -rf $(TEMP_DIR)
	@$(RM) $(TARGET)/*.class

clean-all: clean
	@rm spark*.pdf
