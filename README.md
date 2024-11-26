# Implementation of climbing plant growth simulation

This project is based on the article "Interactive Modeling and Authoring of Climbing Plants" written by T. Hädrich, B. Benes, O. Deussen, and S. Pirk, and it is implemented using Processing on Java.

**author:** Noah DUFAUD

**language:** Java/Processing

## Linux 

### How to config
In the `Makefile`:
- Set the path of the library folder of your Processing installation in the anchor `PROCESSING_PATH`

### How to start

To build and run the simulation, use the following commands:

```bash
make build
make run
```

or simply:

```bash
make
```

## Windows

### How to config

In the `launch.bat` set your Processing installation path in the variable `PROCESSING_PATH`

### How to start

Run the bat script using the terminal

```shell
launch.bat
```

Or double-click the icon in Windows File Explorer.

## Troubleshooting

If your 3D model is too heavy, the JVM may fail to allocate enough memory.

- Increase the maximum memory allocation for the JVM (e.g., from 4G to 8G) with the variable `POWER` in `Makefile` or `launch.bat`.