# set your processing library path
PROCESSING_PATH = /home/dufaudn/.processing-4.3/core/library
POWER = 4G

ARCH = linux-amd64

SRC = src
BIN = build
MAIN = PApp

SRCS = $(shell find $(SRC) -name '*.java')
JARS = $(shell find code -name '*.jar' | tr '\n' ':')

CP = $(PROCESSING_PATH)/core.jar:$(PROCESSING_PATH)/jogl-all.jar:$(PROCESSING_PATH)/gluegen-rt.jar:$(JARS)

all: clean build run

build:
	mkdir -p $(BIN)
	javac -cp "$(CP)" -d $(BIN) $(SRCS)

run:
	java -Xmx$(POWER) -cp "$(BIN):$(CP)" -Djava.library.path=$(PROCESSING_PATH)/$(ARCH) $(MAIN)

clean:
	rm -rf $(BIN)
