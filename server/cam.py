import cv2
import time

def save_cam():
    template = cv2.imread("template.png")

    while True:
        cam = cv2.VideoCapture(0)   # 0 -> index of camera
        s, img = cam.read()
        if s:    # frame captured without any errors
            output = template.copy()
            cam_resized = cv2.resize(img, (64,64))
            output[8:72, 8:72] = cam_resized
            cv2.imwrite("output.png",output) #s

save_cam()