package org.ruru.ffta2editor.utility;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.utility.LZSS.SuffixTree.SuffixMatch;

public class LZSS {
    private static final int N = 4096;
    private static final int F = 18;
    private static final int THRESHOLD = 3;
    //private static final int NIL = N;

    public static class LZSSDecodeResult {
        public ByteBuffer decodedData;
        public int compressedSize;

        public LZSSDecodeResult(ByteBuffer decodedData, int compressedSize) {
            this.decodedData = decodedData;
            this.compressedSize = compressedSize;
        }
    }

    public static class SuffixTree {
        public static class SuffixNode {
            byte[] string;
            int position;
            //SuffixNode child;
            //SuffixNode rightSibling;
            //SuffixNode leftSibling;
            LinkedList<SuffixNode> children;

            public SuffixNode(byte[] string, int position) {
                this.string = string;
                this.position = position;
                children = new LinkedList<>();
            }
        }
        public record SuffixMatch(int position, int length) {}
        SuffixNode root;
        byte[] string; // maybe unnecessary


        public SuffixTree(byte[] string) {
            root = new SuffixNode(new byte[]{}, -1);
            for (int i = 1; i <= string.length; i++) {
                insertString(Arrays.copyOfRange(string, 0, i), 0);
            }
        }

        public void printTree(SuffixNode node, int depth) {
            System.out.println(String.format("%d %d: %s", depth, node.position, IntStream.range(0, node.string.length).mapToObj(i -> node.string[i]).map(x -> Byte.toString(x)).collect(Collectors.joining(" "))));
            for (SuffixNode childNode : node.children) {
                printTree(childNode, depth+1);
            }
        }

        public SuffixMatch find(byte[] suffix) {
            //printTree(root, 0);
            SuffixNode previousNode = null;
            SuffixNode currentNode = root;
            var matchLength = 0;
            Boolean foundMatch = false;
            int i = matchLength;
            int k = 0;
            while (currentNode != null) {
                previousNode = currentNode;
                var childIterator = currentNode.children.iterator();
                matchLength = i;
                i = matchLength;
                k = 0;
                Boolean partialMatch = false;
                SuffixNode childNode = null;
                Boolean lastMatch;

                for (int j = 0; j < currentNode.children.size(); j++) {
                    childNode = childIterator.next();
                    lastMatch = false;
                    //var stringIterator = childNode.string.iterator();
                    for (i = matchLength, k = 0; i < suffix.length && k < childNode.string.length; i++, k++) {
                        if (suffix[i] == childNode.string[k]) {
                            lastMatch = true;
                        } else {
                            lastMatch = false;
                            if (i == matchLength) {
                                // not a match
                                break;
                            } else {
                                // partial match. Branch off
                                partialMatch = true;
                                break;
                            }
                        }
                    }
                    if (lastMatch) {
                        if (i < suffix.length) {
                            if (childNode.children.size() == 0) {
                                return new SuffixMatch(childNode.position, i);
                            } else {
                                // check children
                                foundMatch = true;
                                break;
                            }
                        } else {
                            // Ended mid-edge. Full match. Do nothing
                            return new SuffixMatch(childNode.position, i);
                        }
                    } else if (partialMatch) {
                        // Partial match. End in middle of edge
                        return new SuffixMatch(childNode.position, i);
                    } else {
                        foundMatch = false;
                    }
                    
                }
                if (!foundMatch) break;
                currentNode = childNode;
            }
            if (matchLength > 0) return new SuffixMatch(previousNode.position, matchLength);
            else return new SuffixMatch(0, 0);
        }

        public void remove(byte character, int position) {
            SuffixNode childNode = null;
            var iterator = root.children.iterator();
            int i = 0;
            for (; iterator.hasNext(); i++) {
                childNode = iterator.next();
                if (childNode.string[0] == character) break;
            }
            if (childNode.position == position) {
                root.children.remove(i);
                return;
            }
            SuffixNode currentNode = childNode;
            removePosition(currentNode, position);
        }

        public void removePosition(SuffixNode node, int position) {
            var iterator = node.children.iterator();
            SuffixNode childNode;
            //int i = 0;
            LinkedList<SuffixNode> newChildren = new LinkedList<>();
            //for (; iterator.hasNext(); i++) {
            while (iterator.hasNext()) {
                childNode = iterator.next();
                if (childNode.position == position) {
                    //node.children.remove(i);
                } else {
                    newChildren.add(childNode);
                    removePosition(childNode, position);
                }
            }
            node.children = newChildren;
        }

        public void insertString(byte[] string, int position) {
            for (int i = 0; i < string.length; i++) {
                insertSuffix(Arrays.copyOfRange(string, i, string.length), position+i);
                //printTree(root, 0);
            }
        }

        public void insertSuffix(byte[] suffix, int position) {
            //System.out.println(String.format("insertSuffix: %s", Arrays.stream(suffix).map(x -> Byte.toString(x)).collect(Collectors.joining(" "))));
            SuffixNode previousNode = null;
            SuffixNode currentNode = root;
            //var suffixIterator = suffix.iterator();
            var matchLength = 0;
            Boolean foundMatch = false;
            int i = matchLength;
            int k = 0;
            while (currentNode != null) {
                previousNode = currentNode;
                var childIterator = currentNode.children.iterator();
                matchLength = i;
                i = matchLength;
                k = 0;
                Boolean partialMatch = false;
                SuffixNode childNode = null;
                Boolean lastMatch;

                for (int j = 0; j < currentNode.children.size(); j++) {
                    childNode = childIterator.next();
                    lastMatch = false;
                    //var stringIterator = childNode.string.iterator();
                    for (i = matchLength, k = 0; i < suffix.length && k < childNode.string.length; i++, k++) {
                        if (suffix[i] == childNode.string[k]) {
                            lastMatch = true;
                        } else {
                            lastMatch = false;
                            if (i == matchLength) {
                                // not a match
                                break;
                            } else {
                                // partial match. Branch off
                                partialMatch = true;
                                break;
                            }
                        }
                    }
                    if (lastMatch) {
                        if (i < suffix.length) {
                            if (childNode.children.size() == 0) {
                                childNode.string = Arrays.copyOfRange(suffix, matchLength, suffix.length);
                                return;
                            } else {
                                // check children
                                foundMatch = true;
                                break;
                            }
                        } else {
                            // Ended mid-edge. Full match. Do nothing
                            return;
                        }
                    } else if (partialMatch) {
                        // Partial match. End in middle of edge
                        SuffixNode newInnerNode = new SuffixNode(Arrays.copyOfRange(suffix, matchLength, i), position);
                        currentNode.children.set(j, newInnerNode);
                        newInnerNode.children.add(childNode);
                        childNode.string = Arrays.copyOfRange(childNode.string, k, childNode.string.length);
                        SuffixNode newLeafNode = new SuffixNode(Arrays.copyOfRange(suffix, i, suffix.length), position);
                        newInnerNode.children.add(newLeafNode);
                        matchLength = i;
                        return;
                    } else {
                        foundMatch = false;
                    }
                    
                }
                if (!foundMatch) break;
                currentNode = childNode;
            }
            // Ended at leaf-edge
            SuffixNode newNode = new SuffixNode(Arrays.copyOfRange(suffix, matchLength, suffix.length), position);
            previousNode.children.add(newNode);
            
            
            // Insert child
            //if (foundMatch) {
            //    previousNode.string = Arrays.copyOfRange(suffix, matchLength, suffix.length);
            //}
            //else {
            //    SuffixNode newNode = new SuffixNode(Arrays.copyOfRange(suffix, matchLength, suffix.length), position);
            //    previousNode.children.add(newNode);
            //}
            //previousNode.rightSibling = newNode;
            //newNode.leftSibling = previousNode;
            
        }

    }

    public static ByteBuffer encode(ByteBuffer inFile) {
        return encode(inFile, false);
     }


    public static ByteBuffer encode(ByteBuffer inFile, Boolean vramCompatible) {
        //SuffixTree tree = new SuffixTree(new byte[]{1,2,3,1,2,4});
        ////tree.PrintTree(tree.root, 0);
        //var match = tree.find(new byte[]{2,3,1,3});
        //tree.PrintTree(tree.root, 0);
        //System.out.println(String.format("pos: %d, len: %d", match.position, match.length));



        BitSet flags = new BitSet(8);
        //int startPos = inFile.position();
        inFile.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer outFile = ByteBuffer.allocate(1024*1024).order(ByteOrder.LITTLE_ENDIAN);
        //ByteBuffer copyBuffer = outFile.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);

        int compressionType = 0x10;
        int uncompressedSize = inFile.remaining();
        int header = (uncompressedSize << 8) | compressionType;
        //System.out.println(String.format("Header: %d, %d, %08X", compressionType, uncompressedSize, header));
        outFile.putInt(header);

        byte[] nextBytes = new byte[F];
        //inFile.get(initialBytes);
        SuffixTree suffixTree = new SuffixTree(new byte[0]);


        int windowPos = inFile.position();
        int windowSize = 0;
        int flagPos = outFile.position();
        int flag_index = 7;
        SuffixMatch lastMatch = new SuffixMatch(0, 0);
        int matchLength = 0;
        int matchPosition = 0;
        outFile.position(flagPos+1);
        while (inFile.remaining() > 0) {
            //System.out.println(inFile.remaining());
            if (lastMatch.length > inFile.remaining()) matchLength = inFile.remaining();
            else matchLength = Math.min(lastMatch.length, F);
            if (matchLength < THRESHOLD) {
                //System.out.println("Direct");
                matchLength = 1;
                flags.clear(flag_index);
                outFile.put(inFile.get());
            } else {
                flags.set(flag_index);
                matchPosition = inFile.position() - 1 - lastMatch.position;
                //System.out.println(matchPosition);
                //System.out.println(String.format("Copy: %s, pos: %d, len: %d, inpos: %d", lastMatch, matchPosition, matchLength, inFile.position()));
                byte secondByte = (byte)(matchPosition & 0xFF);
                byte firstByte = (byte)((((matchLength - THRESHOLD) << 4) | matchPosition >>> 8));
                outFile.put(firstByte);
                outFile.put(secondByte);
                inFile.position(inFile.position() + matchLength);
            }
            //flag_index = flag_index == 0 ? 7 : flag_index-1;
            if (flag_index == 0) {
                flag_index = 7;
                outFile.put(flagPos, flags.toByteArray());
                flags.clear();
                flagPos = outFile.position();
                //System.out.println(flagPos);
                outFile.position(flagPos+1);
            } else flag_index--;
            windowSize += matchLength;
            int lastMatchLength = matchLength;
            if (windowSize > N) {
                windowPos += windowSize - N;
            }
            while (windowSize > N) {
                suffixTree.remove(inFile.get(windowPos - (windowSize-N)), windowPos - (windowSize-N));
                windowSize--;
            }
            for (int i = 0; i < lastMatchLength; i++) {
                int newEnd = windowPos + windowSize - lastMatchLength + i;
                suffixTree.insertString(Arrays.copyOfRange(inFile.array(), Math.max(0, newEnd-F), newEnd), Math.max(0, newEnd-F));
                //suffixTree.printTree(suffixTree.root, 0);
            }
            inFile.get(inFile.position(), nextBytes, 0, Math.min(inFile.remaining(), F));
            for (int i = 1; i < 18; i++){
                for (int j = 0; j < 18-i; j++) {
                    if (inFile.position()-18+i+j < 0 || inFile.position()+i > inFile.limit()) continue;
                    if (vramCompatible && i+j == 17 ) continue; // Vram version offset must be at least 2
                    suffixTree.insertSuffix(Arrays.copyOfRange(inFile.array(), inFile.position()-18+i+j, inFile.position()+i), inFile.position()-18+i+j);
                }
            }
            //suffixTree.printTree(suffixTree.root, 0);
            lastMatch = suffixTree.find(nextBytes);
        }
        if (flagPos != 7) {
            outFile.put(flagPos, flags.toByteArray());
        }
        //System.out.println("Encoded");

        ByteBuffer finalOutfile = ByteBuffer.allocate(outFile.position()).order(ByteOrder.LITTLE_ENDIAN);
        outFile.limit(outFile.position()).rewind();
        finalOutfile.put(outFile);

        return finalOutfile.rewind();
    }

    public static LZSSDecodeResult decode(ByteBuffer inFile) throws UnsupportedEncodingException, Exception {
        byte c;
        BitSet flags = new BitSet(8);
        int startPos = inFile.position();
        inFile.order(ByteOrder.LITTLE_ENDIAN);
        
        int header = inFile.getInt();
        int compressionType = (header >>> 4) & 0x0F;
        if (compressionType != 0x1) throw new Exception("Not LZSS compressed");
        int uncompressedSize = header >>> 8;

        if (uncompressedSize < 0) throw new Exception("Uncompressed file too large");
        ByteBuffer outFile = ByteBuffer.allocate(uncompressedSize).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer copyBuffer = outFile.asReadOnlyBuffer();

        int flag_index = 7;
        while(outFile.remaining() > 0) {
            //System.out.println(outFile.remaining());
            if (flag_index == 7) {
                if (inFile.remaining() == 0) break; 
                else {
                    c = inFile.get();
                    flags = BitSet.valueOf(new byte[] {c});
                }
            }
            if (!flags.get(flag_index)) {
                if (inFile.remaining() == 0) break;
                //byte b = inFile.get();
                outFile.put(inFile.get());
            } else {
                if (inFile.remaining() == 0) break;
                int i = Byte.toUnsignedInt(inFile.get());
                if (inFile.remaining() == 0) break;
                int j = Byte.toUnsignedInt(inFile.get());
                int offset = ((i & 0x0F) << 8) | j;
                int num_bytes = ((i & 0xF0) >>> 4) + 3;
                copyBuffer.position(outFile.position() - offset - 1);
                for (int k = 0; k < num_bytes; k++) {
                    outFile.put(copyBuffer.get());
                }
            }
            flag_index = flag_index == 0 ? 7 : flag_index-1;
        }

        return new LZSSDecodeResult(outFile.rewind(), inFile.position() - startPos);
    }
}
