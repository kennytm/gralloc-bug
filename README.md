This is a test case to see if an Android system is able to handle multiple windows from different processes while playing video.

### Setup procedure

1. Upload any mp4 file to `/data/user/jobs.mp4`.
2. Run the GrallocTest project.

### Expected result

1. The video should play
2. After 1 second, numbered boxes should appear and disappear successively. There should be totally **five** number boxes.

### Hacking

* `MainActivity.java` is the activity playing the video and starting all services.
* `TestService.java` defines how the service behaves. It shows the numbered boxes.
* `AndroidManifest.xml` declares the six services (only five are being used). It is marked they will be started in distinct processes.

The rest are just boring boilerplate files.

