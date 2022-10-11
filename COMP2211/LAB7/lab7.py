import cv2

cv2.filter2D

# 'filter2D(src, ddepth, kernel[, dst[, anchor[, delta[, borderType]]]]) -> dst
# @brief Convolves an image with the kernel.

# The function applies an arbitrary linear filter to an image. In-place operation is supported. When
# the aperture is partially outside the image, the function interpolates outlier pixel values
# according to the specified border mode.

# The function does actually compute correlation, not the convolution:

# \\f[\\texttt{dst} (x,y) =  \\sum _{ \\substack{0\\leq x\' < \\texttt{kernel.cols}\\\\{0\\leq y\' < \\texttt{kernel.rows}}}}  \\texttt{kernel} (x\',y\')* \\texttt{src} (x+x\'- \\texttt{anchor.x} ,y+y\'- \\texttt{anchor.y} )\\f]

# That is, the kernel is not mirrored around the anchor point. If you need a real convolution, flip
# the kernel using #flip and set the new anchor to `(kernel.cols - anchor.x - 1, kernel.rows -
# anchor.y - 1)`.

# The function uses the DFT-based algorithm in case of sufficiently large kernels (~`11 x 11` or
# larger) and the direct algorithm for small kernels.

# @param src input image.
# @param dst output image of the same size and the same number of channels as src.
# @param ddepth desired depth of the destination image, see @ref filter_depths "combinations"
# @param kernel convolution kernel (or rather a correlation kernel), a single-channel floating point
# matrix; if you want to apply different kernels to different channels, split the image into
# separate color planes using split and process them individually.
# @param anchor anchor of the kernel that indicates the relative position of a filtered point within
# the kernel; the anchor should lie within the kernel; default value (-1,-1) means that the anchor
# is at the kernel center.
# @param delta optional value added to the filtered pixels before storing them in dst.
# @param borderType pixel extrapolation method, see #BorderTypes. #BORDER_WRAP is not supported.
# @sa  sepFilter2D, dft, matchTemplate'