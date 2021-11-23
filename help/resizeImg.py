import cv2
import os
import numpy as np

def rotate_bound(image, angle):
    # grab the dimensions of the image and then determine the
    # center
    (h, w) = image.shape[:2]
    (cX, cY) = (w // 2, h // 2)
    # grab the rotation matrix (applying the negative of the
    # angle to rotate clockwise), then grab the sine and cosine
    # (i.e., the rotation components of the matrix)
    M = cv2.getRotationMatrix2D((cX, cY), -angle, 1.0)
    cos = np.abs(M[0, 0])
    sin = np.abs(M[0, 1])

    # compute the new bounding dimensions of the image
    nW = int((h * sin) + (w * cos))
    nH = int((h * cos) + (w * sin))

    # adjust the rotation matrix to take into account translation
    M[0, 2] += (nW / 2) - cX
    M[1, 2] += (nH / 2) - cY
    # perform the actual rotation and return the image
    return cv2.warpAffine(image, M, (nW, nH))


# "C:\Users\ELIO\Desktop\smoke_pics\1.jpg"
# width 640  height 480
def resizeAllPic(dir):
    savePath = dir + "resizedPic\\"
    paths =  []
    rpaths = []
    if not os.path.exists(savePath):
        os.mkdir(savePath)

    for file in os.walk(dir):
        rootPath = file[0]
        for pic in file[2]:
            pngName = pic.split(".")[0]+ ".png"
            picName = pic
            picPath = dir + picName
            pngPath = rootPath + pngName
            paths.append(picPath)
            resizePath =savePath + pngName
            rpaths.append(resizePath)

    if not os.listdir(savePath):
        i = 0
        for jpgImg in paths:
            img = cv2.imread(jpgImg)
            res = cv2.resize(img, (640, 480), interpolation=cv2.INTER_CUBIC)
            reImg = rotate_bound(res, -90)
            cv2.imshow("1", reImg)
            cv2.waitKey(100)
            print(rpaths[i])
            cv2.imwrite(rpaths[i], reImg)
            i=i+1




if __name__ == '__main__':
    resizeAllPic("C:\\Users\\ELIO\\Desktop\\smoke_pics\\")
